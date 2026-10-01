package com.egg.homerepair.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ActiveUserFilterTest {

    @Mock
    private UserRepository userRepository;

    private ActiveUserFilter filter;

    @BeforeEach
    void setUp() {
        filter = new ActiveUserFilter(userRepository);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "user@example.test",
                        "n/a",
                        java.util.List.of()));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void inactiveAuthenticatedUserIsLoggedOutBeforeProtectedRequest() throws Exception {
        User inactive = new User();
        inactive.setEmail("user@example.test");
        inactive.setAlta(false);
        when(userRepository.findByEmailIgnoreCase("user@example.test")).thenReturn(inactive);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/home");
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertEquals("/login?inactive=true", response.getRedirectedUrl());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertThrows(IllegalStateException.class, () -> session.getAttribute("userSession"));
    }

    @Test
    void activeAuthenticatedUserContinuesWithFreshSessionUser() throws Exception {
        User active = new User();
        active.setEmail("user@example.test");
        active.setAlta(true);
        when(userRepository.findByEmailIgnoreCase("user@example.test")).thenReturn(active);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/home");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertNull(response.getRedirectedUrl());
        assertSame(active, request.getSession().getAttribute("userSession"));
        assertSame(request, chain.getRequest());
    }
}
