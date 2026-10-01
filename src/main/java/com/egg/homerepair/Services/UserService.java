package com.egg.homerepair.Services;

import com.egg.homerepair.Entities.Image;
import com.egg.homerepair.Entities.User;
import com.egg.homerepair.Entities.Work;
import com.egg.homerepair.Enums.Professions;
import com.egg.homerepair.Enums.Roles;
import com.egg.homerepair.Enums.WorkStatus;
import com.egg.homerepair.Exceptions.MiException;
import com.egg.homerepair.Repositories.UserRepository;
import com.egg.homerepair.Repositories.WorkRepository;
import java.util.ArrayList;
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
        if (validateEmail(user)) {
            throw new MiException("El email ya se encuentra registrado");
        }

        try {
            boolean provider = user.getProfession() != null;
            Image image = imageService.GetByName(provider ? "provider-avatar.png" : "customer-avatar.png");

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
    public User modifyUser(String id, User changes, Image image, boolean changeRole) throws MiException {
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
    public void deleteUser(String id) throws MiException {
        User user = findUser(id);
        user.setAlta(false);
        user.setUnsubscription(new Date());
    }

    @Transactional
    public void updateRole(User user) throws MiException {
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
    public void updateaAlta(User user) throws MiException {
        if (user == null) {
            throw new MiException("Usuario no encontrado");
        }
        user.setAlta(!Boolean.TRUE.equals(user.getAlta()));
        if (Boolean.TRUE.equals(user.getAlta())) {
            user.setUnsubscription(null);
        } else {
            user.setUnsubscription(new Date());
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getById(String id) throws MiException {
        return findUser(id);
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) throws MiException {
        User user = userRepository.searchByEmail(email);
        if (user == null) {
            throw new MiException("Usuario no encontrado");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> userList() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> providerList() {
        return userRepository.findByRole(Roles.PROVIDER);
    }

    @Transactional(readOnly = true)
    public List<User> AllProviderAlta() {
        return userRepository.AllProviderAlta();
    }

    @Transactional(readOnly = true)
    public List<User> ProfessionAlta(Professions profession) {
        return userRepository.searchByProfessionAlta(profession);
    }

    @Transactional(readOnly = true)
    public List<User> AllProfessionAltaFiltro(Professions profession, String search) {
        return userRepository.searchByAllProfessionAltaFiltro(profession, search);
    }

    @Transactional(readOnly = true)
    public List<User> AllAltaFiltro(String search) {
        return userRepository.searchByAllAltaFiltro(search);
    }

    @Transactional(readOnly = true)
    public List<User> providersAndCustomers() {
        List<User> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getRole() != Roles.ADMIN) {
                users.add(user);
            }
        }
        return users;
    }

    @Transactional(readOnly = true)
    public boolean validateEmail(User user) {
        return user != null
                && user.getEmail() != null
                && userRepository.searchByEmail(user.getEmail()) != null;
    }

    @Transactional
    public void updateRating(User provider) {
        if (provider == null) {
            return;
        }

        List<Work> reviewedWorks = new ArrayList<>();
        for (Work work : workRepository.getWorkByUserProvider(provider)) {
            if (work.getWorkStatus() == WorkStatus.REVIEWD) {
                reviewedWorks.add(work);
            }
        }

        int rating = 0;
        if (!reviewedWorks.isEmpty()) {
            double average = reviewedWorks.stream()
                    .mapToInt(Work::getRatingWork)
                    .average()
                    .orElse(0);
            rating = (int) Math.round(average);
        }

        User persistedProvider = userRepository.findById(provider.getId()).orElse(null);
        if (persistedProvider != null) {
            persistedProvider.setRating(rating);
            userRepository.save(persistedProvider);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.searchByEmail(email);

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

    private void replaceImage(User user, Image image) throws MiException {
        if (user.getImage() != null) {
            imageService.Delete(user.getImage());
        }
        imageService.Save(image);
        user.setImage(image.getId());
    }

    private String normalizeOptionalText(String value) {
        return value == null ? null : value.trim();
    }
}

