package com.egg.homerepair.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.egg.homerepair.entity.Image;
import com.egg.homerepair.entity.User;
import com.egg.homerepair.repository.UserRepository;
import com.egg.homerepair.repository.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private WorkRepository workRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, imageService, workRepository, passwordEncoder);
    }

    @Test
    void createCustomerPersistsDefaultAvatarBeforeAssigningItsId() throws Exception {
        User user = new User();
        user.setName("Lucia");
        user.setLastname("Gomez");
        user.setEmail("lucia@example.test");
        user.setPassword("secret123");

        Image defaultAvatar = new Image();

        when(userRepository.findByEmailIgnoreCase("lucia@example.test")).thenReturn(null);
        when(imageService.getByName("customer-avatar.png")).thenReturn(defaultAvatar);
        doAnswer(invocation -> {
            defaultAvatar.setId("image-1");
            return null;
        }).when(imageService).save(defaultAvatar);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret");
        when(userRepository.save(user)).thenReturn(user);

        userService.createUser(user);

        verify(imageService).save(defaultAvatar);
        assertEquals("image-1", user.getImage());
        assertEquals("hashed-secret", user.getPassword());
    }
}
