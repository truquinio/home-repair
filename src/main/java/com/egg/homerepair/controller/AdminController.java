package com.egg.homerepair.controller;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", allUsers());
        return "dashboard";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        String query = search == null ? "" : search.trim();
        List<User> results = query.isEmpty()
                ? Collections.emptyList()
                : userRepository.searchAllUsers(query);

        model.addAttribute("search", query);
        model.addAttribute("se", results);
        model.addAttribute("users", allUsers());
        return "dashboard";
    }

    private List<User> allUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "profession", "lastname", "name"));
    }
}
