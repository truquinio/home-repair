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
        if (emailExists(user.getEmail())) {
            throw new MiException("El email ya se encuentra registrado");
        }

        try {
            boolean provider = user.getProfession() != null;
            Image image = imageService.getByName(
                    provider ? "provider-avatar.png" : "customer-avatar.png");

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

        original.setName(changes.getName());
        original.setLastname(changes.getLastname());

        if (changes.getPassword() != null && !changes.getPassword().trim().isEmpty()) {
            original.setPassword(passwordEncoder.encode(changes.getPassword()));
        }

        return userRepository.save(original);
    }

    @Transactional
    public void deactivateUser(String id) throws MiException {
        User user = findUser(id);
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
