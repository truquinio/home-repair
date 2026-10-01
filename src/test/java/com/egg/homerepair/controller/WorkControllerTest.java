package com.egg.homerepair.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.entity.Work;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.enums.WorkStatus;
import com.egg.homerepair.exception.MiException;
import com.egg.homerepair.repository.UserRepository;
import com.egg.homerepair.repository.WorkRepository;
import com.egg.homerepair.service.UserService;
import com.egg.homerepair.service.WorkService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class WorkControllerTest {

    @Mock
    private WorkService workService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkRepository workRepository;

    @Mock
    private UserService userService;

    private WorkController controller;
    private User customer;
    private User provider;
    private Work persisted;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        controller = new WorkController(workService, userRepository, workRepository, userService);

        customer = user("customer-1", Roles.CUSTOMER);
        provider = user("provider-1", Roles.PROVIDER);

        persisted = new Work();
        persisted.setId("work-1");
        persisted.setUserCustomerId(customer);
        persisted.setUserProviderId(provider);
        persisted.setWorkStatus(WorkStatus.DONE);

        session = new MockHttpSession();
        session.setAttribute("userSession", customer);
    }

    @Test
    void reviewCannotExceedDatabaseLimit() throws Exception {
        Work input = new Work();
        input.setRatingWork(5);
        input.setReview("x".repeat(501));

        when(userRepository.findById("customer-1")).thenReturn(Optional.of(customer));
        when(workService.getById("work-1")).thenReturn(persisted);

        MiException error = assertThrows(
                MiException.class,
                () -> controller.createCheck(input, null, "work-1", session));

        assertEquals("La reseña no puede superar 500 caracteres", error.getMessage());
    }

    private User user(String id, Roles role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        user.setAlta(true);
        return user;
    }
}
