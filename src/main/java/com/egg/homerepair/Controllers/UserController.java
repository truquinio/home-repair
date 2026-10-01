package com.egg.homerepair.Controllers;

import com.egg.homerepair.Entities.Image;
import com.egg.homerepair.Entities.User;
import com.egg.homerepair.Entities.Work;
import com.egg.homerepair.Enums.Professions;
import com.egg.homerepair.Enums.Roles;
import com.egg.homerepair.Exceptions.MiException;
import com.egg.homerepair.Repositories.WorkRepository;
import com.egg.homerepair.Services.ImageService;
import com.egg.homerepair.Services.UserService;
import com.egg.homerepair.converters.ImageConverter;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final ImageConverter imageConverter;
    private final WorkRepository workRepository;

    public UserController(
            UserService userService,
            ImageService imageService,
            ImageConverter imageConverter,
            WorkRepository workRepository) {
        this.userService = userService;
        this.imageService = imageService;
        this.imageConverter = imageConverter;
        this.workRepository = workRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("professions", Professions.values());
        return "registerUser";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) throws MiException {
        if (userService.validateEmail(user)) {
            model.addAttribute("mssg", "El email ingresado ya se encuentra registrado ðŸš«");
            model.addAttribute("professions", Professions.values());
            return "registerUser";
        }

        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/perfil/{id}")
    public String profile(
            @PathVariable String id,
            Model model,
            HttpSession session) throws MiException {

        User target = userService.getById(id);
        User actor = sessionUser(session);

        model.addAttribute("user", target);
        addReviews(model, target);

        if (actor == null) {
            return "redirect:/login";
        }

        if (target.getId().equals(actor.getId()) || actor.getRole() == Roles.ADMIN) {
            model.addAttribute("professions", Professions.values());
            return "myProfile";
        }

        if (actor.getRole() == Roles.CUSTOMER && target.getRole() == Roles.PROVIDER) {
            return "otherProfile";
        }

        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/perfil/{id}/review")
    public String reviewsForAdmin(@PathVariable String id, Model model) throws MiException {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        addReviews(model, user);
        return "otherProfile";
    }

    @PostMapping(value = "/perfil/{id}/mod", consumes = "multipart/form-data")
    public String edit(
            @PathVariable String id,
            @ModelAttribute User changes,
            @RequestParam("img") MultipartFile file,
            HttpSession session) throws MiException {

        User actor = requireSessionUser(session);
        boolean ownProfile = id.equals(actor.getId());

        if (!ownProfile && actor.getRole() != Roles.ADMIN) {
            throw new MiException("No tenÃ©s permisos para editar este perfil");
        }

        Image image = file.isEmpty() ? null : imageConverter.convert(file);
        User updated = userService.modifyUser(id, changes, image, false);

        if (ownProfile) {
            session.setAttribute("userSession", updated);
            return "redirect:/user/perfil/" + id;
        }

        return "redirect:/admin/dashboard";
    }

    @PostMapping(value = "/perfil/{id}/change", consumes = "multipart/form-data")
    public String changeRole(
            @PathVariable String id,
            @ModelAttribute User changes,
            @RequestParam("img") MultipartFile file,
            HttpSession session) throws MiException {

        User actor = requireSessionUser(session);
        if (!id.equals(actor.getId()) || actor.getRole() == Roles.ADMIN) {
            throw new MiException("No podÃ©s cambiar este perfil");
        }

        Image image = file.isEmpty() ? defaultImageForRoleChange(actor) : imageConverter.convert(file);
        User updated = userService.modifyUser(id, changes, image, true);
        session.setAttribute("userSession", updated);

        // The granted authorities must be recreated after a role change.
        return "redirect:/logout";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/perfil/{id}/role")
    public String editRole(@PathVariable String id) throws MiException {
        userService.updateRole(userService.getById(id));
        return "redirect:/admin/dashboard";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/perfil/{id}/alta")
    public String editAlta(@PathVariable String id) throws MiException {
        userService.updateaAlta(userService.getById(id));
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/perfil/{id}/del")
    public String delete(@PathVariable String id, HttpSession session) throws MiException {
        User actor = requireSessionUser(session);
        boolean ownAccount = id.equals(actor.getId());

        if (!ownAccount && actor.getRole() != Roles.ADMIN) {
            throw new MiException("No tenÃ©s permisos para eliminar esta cuenta");
        }

        userService.deleteUser(id);

        if (ownAccount) {
            return "redirect:/logout";
        }
        return "redirect:/admin/dashboard";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.userList());
        return "userList";
    }

    private void addReviews(Model model, User user) {
        List<Work> reviews = workRepository.getWorkByUserProvider(user);
        model.addAttribute("check", reviews.isEmpty() ? "false" : "");
        model.addAttribute("listReviews", reviews);
    }

    private User sessionUser(HttpSession session) {
        Object value = session.getAttribute("userSession");
        return value instanceof User ? (User) value : null;
    }

    private User requireSessionUser(HttpSession session) throws MiException {
        User user = sessionUser(session);
        if (user == null) {
            throw new MiException("DebÃ©s iniciar sesiÃ³n");
        }
        return userService.getById(user.getId());
    }

    private Image defaultImageForRoleChange(User current) throws MiException {
        Image currentImage = imageService.GetById(current.getImage());
        if (currentImage == null) {
            return null;
        }

        try {
            if (current.getRole() == Roles.CUSTOMER
                    && "customer-avatar.png".equals(currentImage.getName())) {
                return imageService.GetByName("provider-avatar.png");
            }

            if (current.getRole() == Roles.PROVIDER
                    && "provider-avatar.png".equals(currentImage.getName())) {
                return imageService.GetByName("customer-avatar.png");
            }

            return null;
        } catch (IOException e) {
            throw new MiException("No se pudo cargar la imagen por defecto");
        }
    }
}

