package com.egg.homerepair.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.egg.homerepair.entity.Image;
import com.egg.homerepair.entity.User;
import com.egg.homerepair.enums.Professions;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.exception.MiException;
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
    void createUserRejectsBlankNameBeforeProcessingAvatarOrPassword() {
        User user = validUser("lucia@example.test");
        user.setName(" ");

        MiException error = assertThrows(MiException.class, () -> userService.createUser(user));

        assertEquals("El nombre es obligatorio", error.getMessage());
        verifyNoInteractions(imageService, passwordEncoder);
    }

    @Test
    void createUserRejectsMalformedEmailBeforeProcessingAvatarOrPassword() {
        User user = validUser("not-an-email");

        MiException error = assertThrows(MiException.class, () -> userService.createUser(user));

        assertEquals("El email no es válido", error.getMessage());
        verifyNoInteractions(imageService, passwordEncoder);
    }

    @Test
    void createUserRejectsShortPasswordBeforeProcessingAvatarOrPassword() {
        User user = validUser("lucia@example.test");
        user.setPassword("123");

        MiException error = assertThrows(MiException.class, () -> userService.createUser(user));

        assertEquals("La contraseña debe tener al menos 6 caracteres", error.getMessage());
        verifyNoInteractions(imageService, passwordEncoder);
    }

    @Test
    void customerCannotBecomeProviderWithoutProfession() {
        User customer = validUser("customer@example.test");
        customer.setId("customer-1");
        customer.setRole(Roles.CUSTOMER);

        MiException error = assertThrows(
                MiException.class,
                () -> userService.toggleRole(customer));

        assertEquals("Elegí una profesión antes de convertir la cuenta en proveedor", error.getMessage());
    }

    @Test
    void profileRoleChangeCannotCreateProviderWithoutProfession() {
        User customer = validUser("profile-customer@example.test");
        customer.setId("customer-2");
        customer.setRole(Roles.CUSTOMER);

        User changes = validUser("profile-customer@example.test");
        changes.setProfession(null);

        when(userRepository.findById("customer-2"))
                .thenReturn(java.util.Optional.of(customer));

        MiException error = assertThrows(
                MiException.class,
                () -> userService.modifyUser("customer-2", changes, null, true));

        assertEquals("Elegí una profesión antes de convertir la cuenta en proveedor", error.getMessage());
    }

    @Test
    void adminAccountCannotBeDeactivated() {
        User admin = validUser("admin@example.test");
        admin.setId("admin-1");
        admin.setRole(Roles.ADMIN);
        admin.setAlta(true);

        when(userRepository.findById("admin-1")).thenReturn(java.util.Optional.of(admin));

        MiException error = assertThrows(
                MiException.class,
                () -> userService.deactivateUser("admin-1"));

        assertEquals("Las cuentas administrativas no pueden desactivarse desde esta acción", error.getMessage());
    }

    @Test
    void adminAccountCannotBeToggledInactive() {
        User admin = validUser("admin-toggle@example.test");
        admin.setId("admin-2");
        admin.setRole(Roles.ADMIN);
        admin.setAlta(true);

        MiException error = assertThrows(
                MiException.class,
                () -> userService.toggleActiveStatus(admin));

        assertEquals("Las cuentas administrativas no pueden desactivarse desde esta acción", error.getMessage());
    }

    @Test
    void modifyUserRejectsBlankName() {
        User existing = validUser("edit@example.test");
        existing.setId("user-edit");
        existing.setRole(Roles.CUSTOMER);

        User changes = validUser("edit@example.test");
        changes.setName(" ");

        when(userRepository.findById("user-edit"))
                .thenReturn(java.util.Optional.of(existing));

        MiException error = assertThrows(
                MiException.class,
                () -> userService.modifyUser("user-edit", changes, null, false));

        assertEquals("El nombre es obligatorio", error.getMessage());
    }

    @Test
    void modifyUserRejectsShortNewPassword() {
        User existing = validUser("password@example.test");
        existing.setId("user-password");
        existing.setRole(Roles.CUSTOMER);

        User changes = validUser("password@example.test");
        changes.setPassword("123");

        when(userRepository.findById("user-password"))
                .thenReturn(java.util.Optional.of(existing));

        MiException error = assertThrows(
                MiException.class,
                () -> userService.modifyUser("user-password", changes, null, false));

        assertEquals("La contraseña debe tener al menos 6 caracteres", error.getMessage());
    }

    @Test
    void providerEditRequiresProfessionAndFieldLimits() {
        User existing = validUser("provider@example.test");
        existing.setId("provider-edit");
        existing.setRole(Roles.PROVIDER);
        existing.setProfession(Professions.PLOMERO);

        User changes = validUser("provider@example.test");
        changes.setProfession(null);
        changes.setDescription("x".repeat(501));
        changes.setPhone("1".repeat(31));

        when(userRepository.findById("provider-edit"))
                .thenReturn(java.util.Optional.of(existing));

        MiException error = assertThrows(
                MiException.class,
                () -> userService.modifyUser("provider-edit", changes, null, false));

        assertEquals("La profesión es obligatoria para proveedores", error.getMessage());
    }

    @Test
    void createUserIgnoresClientSuppliedId() throws Exception {
        User user = validUser("lucia-id@example.test");
        user.setId("existing-user-id");

        Image defaultAvatar = new Image();
        defaultAvatar.setId("image-1");

        when(userRepository.findByEmailIgnoreCase("lucia-id@example.test")).thenReturn(null);
        when(imageService.getByName("customer-avatar.png")).thenReturn(defaultAvatar);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret");
        when(userRepository.save(user)).thenReturn(user);

        userService.createUser(user);

        assertNull(user.getId());
    }

    @Test
    void createCustomerPersistsDefaultAvatarBeforeAssigningItsId() throws Exception {
        User user = validUser("lucia@example.test");
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

    private User validUser(String email) {
        User user = new User();
        user.setName("Lucia");
        user.setLastname("Gomez");
        user.setEmail(email);
        user.setPassword("secret123");
        return user;
    }
}
