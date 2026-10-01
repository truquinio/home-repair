package com.egg.homerepair.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.entity.Work;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.enums.WorkStatus;
import com.egg.homerepair.exception.MiException;
import com.egg.homerepair.repository.WorkRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    private WorkService workService;
    private User customer;
    private User provider;
    private Work work;

    @BeforeEach
    void setUp() {
        workService = new WorkService(workRepository);

        customer = user("customer", Roles.CUSTOMER);
        provider = user("provider", Roles.PROVIDER);

        work = new Work();
        work.setId("work-1");
        work.setCustomer(customer);
        work.setProvider(provider);
        work.setWorkStatus(WorkStatus.REQUIRED);
    }

    @Test
    void createWorkRejectsBlankOrOversizedContent() {
        Work blank = new Work();
        blank.setCustomer(customer);
        blank.setProvider(provider);
        blank.setWorkName(" ");
        blank.setWorkDescription("Valid description");

        Work tooLong = new Work();
        tooLong.setCustomer(customer);
        tooLong.setProvider(provider);
        tooLong.setWorkName("x".repeat(101));
        tooLong.setWorkDescription("Valid description");

        assertThrows(MiException.class, () -> workService.createWork(blank));
        assertThrows(MiException.class, () -> workService.createWork(tooLong));
    }

    @Test
    void adminCannotReopenReviewedWork() {
        User admin = user("admin", Roles.ADMIN);
        work.setWorkStatus(WorkStatus.REVIEWD);
        when(workRepository.findById("work-1")).thenReturn(Optional.of(work));

        assertThrows(
                MiException.class,
                () -> workService.changeWorkStatus("work-1", "REVERT", admin));

        assertEquals(WorkStatus.REVIEWD, work.getWorkStatus());
    }

    @Test
    void createWorkIgnoresClientSuppliedId() throws MiException {
        Work submitted = new Work();
        submitted.setId("existing-work-id");
        submitted.setCustomer(customer);
        submitted.setProvider(provider);
        submitted.setWorkName("Repair leak");
        submitted.setWorkDescription("Water is leaking under the sink.");
        submitted.setReview("Injected review");
        submitted.setRatingWork(5);

        workService.createWork(submitted);

        assertNull(submitted.getId());
        assertEquals(WorkStatus.REQUIRED, submitted.getWorkStatus());
        assertNull(submitted.getReview());
        assertEquals(0, submitted.getRatingWork());
        verify(workRepository).save(submitted);
    }

    @Test
    void providerCanAcceptOwnRequestedWork() throws MiException {
        when(workRepository.findById("work-1")).thenReturn(Optional.of(work));

        workService.changeWorkStatus("work-1", "ACCEPTED", provider);

        assertEquals(WorkStatus.ACCEPTED, work.getWorkStatus());
    }

    @Test
    void customerCannotAcceptRequestedWork() {
        when(workRepository.findById("work-1")).thenReturn(Optional.of(work));

        assertThrows(
                MiException.class,
                () -> workService.changeWorkStatus("work-1", "ACCEPTED", customer));

        assertEquals(WorkStatus.REQUIRED, work.getWorkStatus());
    }

    @Test
    void unrelatedProviderCannotModifyWork() {
        User otherProvider = user("provider-2", Roles.PROVIDER);
        when(workRepository.findById("work-1")).thenReturn(Optional.of(work));

        assertThrows(
                MiException.class,
                () -> workService.changeWorkStatus("work-1", "REVERT", otherProvider));

        assertEquals(WorkStatus.REQUIRED, work.getWorkStatus());
    }

    @Test
    void missingWorkIsRejected() {
        when(workRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(
                MiException.class,
                () -> workService.changeWorkStatus("missing", "DONE", provider));
    }

    private User user(String id, Roles role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        user.setAlta(true);
        return user;
    }
}
