package com.egg.homerepair.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PageRenderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void publicPagesRenderWithoutTemplateErrors() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Home Repair")));

        mockMvc.perform(get("/about"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/search"))
                .andExpect(status().isOk());
    }

    @Test
    void authenticatedHomeRendersPostLogoutForm() throws Exception {
        User sessionUser = new User();
        sessionUser.setId("customer-1");
        sessionUser.setName("Lucia");
        sessionUser.setLastname("Gomez");
        sessionUser.setEmail("lucia@example.test");
        sessionUser.setPassword("test-password-hash");
        sessionUser.setRole(Roles.CUSTOMER);
        sessionUser.setAlta(true);
        sessionUser.setImage("image-1");
        userRepository.saveAndFlush(sessionUser);

        mockMvc.perform(get("/home")
                        .with(user("lucia@example.test").roles("CUSTOMER"))
                        .sessionAttr("userSession", sessionUser))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Home Repair")))
                .andExpect(content().string(containsString("action=\"/logout\"")))
                .andExpect(content().string(containsString("method=\"post\"")));
    }
}
