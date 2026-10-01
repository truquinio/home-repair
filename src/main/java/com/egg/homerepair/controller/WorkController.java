package com.egg.homerepair.controller;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.entity.Work;
import com.egg.homerepair.enums.Roles;
import com.egg.homerepair.enums.WorkStatus;
import com.egg.homerepair.exception.MiException;
import com.egg.homerepair.repository.UserRepository;
import com.egg.homerepair.repository.WorkRepository;
import com.egg.homerepair.service.UserService;
import com.egg.homerepair.service.WorkService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/work")
public class WorkController {

    private final WorkService workService;
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final UserService userService;

    public WorkController(
            WorkService workService,
            UserRepository userRepository,
            WorkRepository workRepository,
            UserService userService) {
        this.workService = workService;
        this.userRepository = userRepository;
        this.workRepository = workRepository;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String create(
            @RequestParam("applicationType") String applicationType,
            @RequestParam(value = "idProvider", required = false) String idProvider,
            @RequestParam(value = "idWork", required = false) String idWork,
            ModelMap model,
            HttpSession session) throws MiException {

        User actor = requireSessionUser(session);

        if ("review".equals(applicationType)) {
            Work work = workService.getById(idWork);
            if (actor.getRole() != Roles.CUSTOMER
                    || !actor.getId().equals(work.getCustomer().getId())
                    || work.getWorkStatus() != WorkStatus.DONE) {
                throw new MiException("No podés valorar este trabajo");
            }

            model.addAttribute("workReview", work);
            model.addAttribute("applicationType", applicationType);
            model.addAttribute("idWork", work.getId());
            return "registerWork";
        }

        if ("work".equals(applicationType)) {
            if (actor.getRole() != Roles.CUSTOMER) {
                throw new MiException("Sólo los clientes pueden solicitar trabajos");
            }

            User provider = userRepository.findById(idProvider)
                    .filter(user -> user.getRole() == Roles.PROVIDER && Boolean.TRUE.equals(user.getAlta()))
                    .orElseThrow(() -> new MiException("Proveedor no disponible"));

            model.addAttribute("applicationType", applicationType);
            model.addAttribute("idProvider", provider.getId());
            model.addAttribute("work", new Work());
            return "registerWork";
        }

        throw new MiException("Tipo de operación inválido");
    }

    @PostMapping("/create")
    public String createCheck(
            @ModelAttribute Work work,
            @RequestParam(value = "idProvider", required = false) String idProvider,
            @RequestParam(value = "idWork", required = false) String idWork,
            HttpSession session) throws MiException {

        User actor = requireSessionUser(session);

        if (idWork != null && idProvider == null) {
            addReview(idWork, work, actor);
            return "redirect:/home";
        }

        if (actor.getRole() != Roles.CUSTOMER) {
            throw new MiException("Sólo los clientes pueden solicitar trabajos");
        }

        User provider = userRepository.findById(idProvider)
                .filter(user -> user.getRole() == Roles.PROVIDER && Boolean.TRUE.equals(user.getAlta()))
                .orElseThrow(() -> new MiException("Proveedor no disponible"));

        work.setCustomer(actor);
        work.setProvider(provider);
        workService.createWork(work);
        return "redirect:/home";
    }

    @GetMapping("/worksList")
    public String worksList(HttpSession session, ModelMap model) throws MiException {
        User user = requireSessionUser(session);

        if (user.getRole() == Roles.PROVIDER) {
            List<Work> works = workRepository.findByProvider(user);
            model.addAttribute("providerWorkList", works);
            return "worksUser";
        }

        if (user.getRole() == Roles.CUSTOMER) {
            List<Work> works = workRepository.findByCustomer(user);
            model.addAttribute("customerWorkList", works);
            return "worksUser";
        }

        return "redirect:/home";
    }

    @PostMapping("/status")
    public String updateStatus(
            @RequestParam("idWork") String id,
            @RequestParam("wStat") String status,
            HttpSession session) throws MiException {
        User user = requireSessionUser(session);
        workService.changeWorkStatus(id, status, user);
        return "redirect:/work/worksList";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deleteComment")
    public String deleteComment(
            @RequestParam("idWork") String idWork) throws MiException {

        Work work = workService.getById(idWork);
        work.setReview("Comentario moderado por administración.");
        workRepository.save(work);
        return "redirect:/admin/dashboard";
    }

    private void addReview(String idWork, Work reviewInput, User actor) throws MiException {
        Work persisted = workService.getById(idWork);

        if (actor.getRole() != Roles.CUSTOMER
                || persisted.getCustomer() == null
                || !actor.getId().equals(persisted.getCustomer().getId())
                || persisted.getWorkStatus() != WorkStatus.DONE) {
            throw new MiException("No podés valorar este trabajo");
        }

        int rating = reviewInput.getRatingWork();
        String review = reviewInput.getReview() == null ? "" : reviewInput.getReview().trim();

        if (rating < 1 || rating > 5) {
            throw new MiException("La calificación debe estar entre 1 y 5");
        }
        if (review.isEmpty()) {
            throw new MiException("La reseña no puede estar vacía");
        }
        if (review.length() > 500) {
            throw new MiException("La reseña no puede superar 500 caracteres");
        }

        persisted.setReview(review);
        persisted.setRatingWork(rating);
        persisted.setWorkStatus(WorkStatus.REVIEWD);
        workRepository.save(persisted);
        userService.updateRating(persisted.getProvider());
    }

    private User requireSessionUser(HttpSession session) throws MiException {
        Object value = session.getAttribute("userSession");
        if (!(value instanceof User)) {
            throw new MiException("Debés iniciar sesión");
        }

        User user = (User) value;
        User persisted = userRepository.findById(user.getId())
                .orElseThrow(() -> new MiException("Usuario no disponible"));
        if (!Boolean.TRUE.equals(persisted.getAlta())) {
            throw new MiException("Usuario no disponible");
        }
        return persisted;
    }
}
