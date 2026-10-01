package com.egg.homerepair.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.enums.Professions;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.repository.WorkRepository;
import com.egg.homerepair.service.ImageService;
import com.egg.homerepair.service.UserService;
import com.egg.homerepair.converter.ImageConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ImageService imageService;

    @Mock
    private ImageConverter imageConverter;

    @Mock
    private WorkRepository workRepository;

    private UserController controller;
    private User customer;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        controller = new UserController(
                userService,
                imageService,
                imageConverter,
                workRepository);

        customer = new User();
        customer.setId("customer-1");
        customer.setName("Lucía");
        customer.setLastname("Gómez");
        customer.setRole(Roles.CUSTOMER);
        customer.setAlta(true);

        session = new MockHttpSession();
        session.setAttribute("userSession", customer);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("lucia@example.test", "n/a"));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void ownRoleChangeInvalidatesSessionAndAuthentication() throws Exception {
        User changes = new User();
        changes.setName("Lucía");
        changes.setLastname("Gómez");
        changes.setProfession(Professions.PLOMERO);

        when(userService.getById("customer-1")).thenReturn(customer);
        when(userService.modifyUser(eq("customer-1"), eq(changes), any(), eq(true)))
                .thenReturn(customer);

        String view = controller.changeRole(
                "customer-1",
                changes,
                new MockMultipartFile("img", new byte[0]),
                session);

        assertEquals("redirect:/login", view);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertThrows(IllegalStateException.class, () -> session.getAttribute("userSession"));
        verify(userService).modifyUser(eq("customer-1"), eq(changes), any(), eq(true));
    }

    @Test
    void ownDeactivationInvalidatesSessionAndAuthentication() throws Exception {
        when(userService.getById("customer-1")).thenReturn(customer);

        String view = controller.delete("customer-1", session);

        assertEquals("redirect:/login", view);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertThrows(IllegalStateException.class, () -> session.getAttribute("userSession"));
        verify(userService).deactivateUser("customer-1");
    }
}
