package com.egg.MiMaridoTeLoHace.Controllers;

import com.egg.MiMaridoTeLoHace.Entities.User;
import com.egg.MiMaridoTeLoHace.Enums.Professions;
import com.egg.MiMaridoTeLoHace.Enums.Roles;
import com.egg.MiMaridoTeLoHace.Exceptions.MiException;
import com.egg.MiMaridoTeLoHace.Services.UserService;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalController {

    private final UserService userService;

    public PortalController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_PROVIDER', 'ROLE_ADMIN')")
    @GetMapping("/home")
    public String home(HttpSession session) {
        Object value = session.getAttribute("userSession");
        if (!(value instanceof User)) {
            return "redirect:/login";
        }

        User user = (User) value;
        if (user.getRole() == Roles.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        return "home";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("mssg", "Usuario o contraseña inválidos 🚫");
        }
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/search")
    public String showProviders(
            @RequestParam(name = "profession", defaultValue = "") String profession,
            @RequestParam(name = "st", defaultValue = "") String search,
            ModelMap model) {

        String professionValue = profession.trim();
        String searchValue = search.trim();
        List<User> results;

        if (professionValue.isEmpty() && searchValue.isEmpty()) {
            results = userService.AllProviderAlta();
        } else if (professionValue.isEmpty()) {
            results = userService.AllAltaFiltro(searchValue);
        } else {
            Professions parsedProfession = parseProfession(professionValue);
            if (parsedProfession == null) {
                results = Collections.emptyList();
            } else if (searchValue.isEmpty()) {
                results = userService.ProfessionAlta(parsedProfession);
            } else {
                results = userService.AllProfessionAltaFiltro(parsedProfession, searchValue);
            }
        }

        model.addAttribute("professions", Professions.values());
        model.addAttribute("searchReturn", results);
        return "provider";
    }

    private Professions parseProfession(String value) {
        try {
            return Professions.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
