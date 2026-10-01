package com.egg.homerepair.service;

import com.egg.homerepair.entity.Image;
import com.egg.homerepair.entity.User;
import com.egg.homerepair.entity.Work;
import com.egg.homerepair.enums.Professions;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.enums.WorkStatus;
import com.egg.homerepair.exception.MiException;
import com.egg.homerepair.repository.UserRepository;
import com.egg.homerepair.repository.WorkRepository;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final WorkRepository workRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            ImageService imageService,
            WorkRepository workRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.workRepository = workRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(User user) throws MiException {
        if (user == null) {
            throw new MiException("El usuario no puede ser nulo");
        }

        validateNewUser(user);

        if (emailExists(user.getEmail())) {
            throw new MiException("El email ya se encuentra registrado");
        }

        user.setId(null);

        try {
            boolean provider = user.getProfession() != null;
            Image image = imageService.getByName(
                    provider ? "provider-avatar.png" : "customer-avatar.png");
            imageService.save(image);

            user.setRole(provider ? Roles.PROVIDER : Roles.CUSTOMER);
            user.setRating(0);
            user.setImage(image.getId());
            user.setAlta(true);
            user.setSubscription(new Date());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        } catch (Exception e) {
            throw new MiException("Error al crear usuario");
        }
    }

    @Transactional
    public User modifyUser(String id, User changes, Image image, boolean changeRole)
            throws MiException {
        User original = findUser(id);
        if (changes == null) {
            throw new MiException("Los datos del perfil son obligatorios");
        }

        Roles targetRole = original.getRole();
        if (changeRole && original.getRole() == Roles.CUSTOMER) {
            targetRole = Roles.PROVIDER;
        } else if (changeRole && original.getRole() == Roles.PROVIDER) {
            targetRole = Roles.CUSTOMER;
        }

        validateProfileChanges(changes, targetRole);

        if (changeRole) {
            toggleCustomerProvider(original);
        }

        if (original.getRole() == Roles.PROVIDER) {
            original.setDescription(normalizeOptionalText(changes.getDescription()));
            original.setProfession(changes.getProfession());
            original.setPhone(normalizeOptionalText(changes.getPhone()));
        }

        if (image != null) {
            replaceImage(original, image);
        }

        original.setName(changes.getName().trim());
        original.setLastname(changes.getLastname().trim());

        if (changes.getPassword() != null && !changes.getPassword().trim().isEmpty()) {
            original.setPassword(passwordEncoder.encode(changes.getPassword()));
        }

        return userRepository.save(original);
    }

    @Transactional
    public void deactivateUser(String id) throws MiException {
        User user = findUser(id);
        ensureNonAdminLifecycleChange(user);
        user.setAlta(false);
        user.setUnsubscription(new Date());
    }

    @Transactional
    public void toggleRole(User user) throws MiException {
        if (user == null) {
            throw new MiException("Usuario no encontrado");
        }

        if (user.getRole() == Roles.PROVIDER) {
            clearProviderFields(user);
            user.setRole(Roles.CUSTOMER);
        } else if (user.getRole() == Roles.CUSTOMER) {
            if (user.getProfession() == null) {
                throw new MiException("Elegí una profesión antes de convertir la cuenta en proveedor");
            }
            user.setRole(Roles.PROVIDER);
            user.setRating(0);
        }

        userRepository.save(user);
    }

    @Transactional
    public void toggleActiveStatus(User user) throws MiException {
        if (user == null) {
            throw new MiException("Usuario no encontrado");
        }
        ensureNonAdminLifecycleChange(user);

        user.setAlta(!Boolean.TRUE.equals(user.getAlta()));
        user.setUnsubscription(Boolean.TRUE.equals(user.getAlta()) ? null : new Date());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getById(String id) throws MiException {
        return findUser(id);
    }

    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> findActiveProviders() {
        return userRepository.findByRoleAndAltaTrueOrderByRatingDesc(Roles.PROVIDER);
    }

    @Transactional(readOnly = true)
    public List<User> findActiveProvidersByProfession(Professions profession) {
        return userRepository.findByRoleAndAltaTrueAndProfessionOrderByRatingDesc(
                Roles.PROVIDER,
                profession);
    }

    @Transactional(readOnly = true)
    public List<User> findActiveProvidersByProfessionAndSearch(
            Professions profession,
            String search) {
        return userRepository.searchActiveProvidersByProfession(
                Roles.PROVIDER,
                profession,
                search);
    }

    @Transactional(readOnly = true)
    public List<User> findActiveProvidersBySearch(String search) {
        return userRepository.searchActiveProviders(Roles.PROVIDER, search);
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return email != null
                && !email.trim().isEmpty()
                && userRepository.findByEmailIgnoreCase(email.trim()) != null;
    }

    @Transactional
    public void updateRating(User provider) {
        if (provider == null) {
            return;
        }

        double average = workRepository.getWorkByUserProvider(provider).stream()
                .filter(work -> work.getWorkStatus() == WorkStatus.REVIEWD)
                .mapToInt(Work::getRatingWork)
                .average()
                .orElse(0);

        userRepository.findById(provider.getId()).ifPresent(persistedProvider -> {
            persistedProvider.setRating((int) Math.round(average));
            userRepository.save(persistedProvider);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = email == null
                ? null
                : userRepository.findByEmailIgnoreCase(email.trim());

        if (user == null || !Boolean.TRUE.equals(user.getAlta())) {
            throw new UsernameNotFoundException("Usuario no encontrado o inactivo");
        }

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(true);
            session.setAttribute("userSession", user);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    private void validateProfileChanges(User changes, Roles targetRole) throws MiException {
        String name = normalizeRequired(changes.getName(), "El nombre es obligatorio");
        String lastname = normalizeRequired(changes.getLastname(), "El apellido es obligatorio");
        if (name.length() > 60) {
            throw new MiException("El nombre no puede superar 60 caracteres");
        }
        if (lastname.length() > 60) {
            throw new MiException("El apellido no puede superar 60 caracteres");
        }

        String password = changes.getPassword();
        if (password != null && !password.trim().isEmpty()) {
            validatePassword(password);
        }

        if (targetRole == Roles.PROVIDER) {
            if (changes.getProfession() == null) {
                throw new MiException("La profesión es obligatoria para proveedores");
            }
            String phone = normalizeOptionalText(changes.getPhone());
            String description = normalizeOptionalText(changes.getDescription());
            if (phone != null && phone.length() > 30) {
                throw new MiException("El teléfono no puede superar 30 caracteres");
            }
            if (description != null && description.length() > 500) {
                throw new MiException("La descripción no puede superar 500 caracteres");
            }
        }
    }

    private void validateNewUser(User user) throws MiException {
        String name = normalizeRequired(user.getName(), "El nombre es obligatorio");
        String lastname = normalizeRequired(user.getLastname(), "El apellido es obligatorio");
        if (name.length() > 60) {
            throw new MiException("El nombre no puede superar 60 caracteres");
        }
        if (lastname.length() > 60) {
            throw new MiException("El apellido no puede superar 60 caracteres");
        }
        user.setName(name);
        user.setLastname(lastname);

        String email = normalizeRequired(user.getEmail(), "El email es obligatorio").toLowerCase();
        if (email.length() > 120) {
            throw new MiException("El email no puede superar 120 caracteres");
        }
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new MiException("El email no es válido");
        }
        user.setEmail(email);

        validatePassword(user.getPassword());

        if (user.getProfession() != null) {
            String phone = normalizeOptionalText(user.getPhone());
            String description = normalizeOptionalText(user.getDescription());
            if (phone != null && phone.length() > 30) {
                throw new MiException("El teléfono no puede superar 30 caracteres");
            }
            if (description != null && description.length() > 500) {
                throw new MiException("La descripción no puede superar 500 caracteres");
            }
        }
    }

    private void validatePassword(String password) throws MiException {
        if (password == null || password.length() < 6) {
            throw new MiException("La contraseña debe tener al menos 6 caracteres");
        }
        if (password.length() > 72) {
            throw new MiException("La contraseña no puede superar 72 caracteres");
        }
    }

    private String normalizeRequired(String value, String message) throws MiException {
        if (value == null || value.trim().isEmpty()) {
            throw new MiException(message);
        }
        return value.trim();
    }

    private User findUser(String id) throws MiException {
        if (id == null || id.trim().isEmpty()) {
            throw new MiException("El identificador del usuario es obligatorio");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new MiException("Usuario no encontrado"));
    }

    private void toggleCustomerProvider(User user) {
        if (user.getRole() == Roles.CUSTOMER) {
            user.setRole(Roles.PROVIDER);
            user.setRating(0);
        } else if (user.getRole() == Roles.PROVIDER) {
            clearProviderFields(user);
            user.setRole(Roles.CUSTOMER);
        }
    }

    private void ensureNonAdminLifecycleChange(User user) throws MiException {
        if (user.getRole() == Roles.ADMIN) {
            throw new MiException("Las cuentas administrativas no pueden desactivarse desde esta acción");
        }
    }

    private void clearProviderFields(User user) {
        user.setDescription(null);
        user.setProfession(null);
        user.setPhone(null);
        user.setRating(0);
    }

    private void replaceImage(User user, Image image) {
        if (user.getImage() != null) {
            imageService.delete(user.getImage());
        }
        imageService.save(image);
        user.setImage(image.getId());
    }

    private String normalizeOptionalText(String value) {
        return value == null ? null : value.trim();
    }
}
