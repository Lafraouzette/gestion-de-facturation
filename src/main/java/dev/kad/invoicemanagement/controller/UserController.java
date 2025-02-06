package dev.kad.invoicemanagement.controller;

import dev.kad.invoicemanagement.model.dto.UserClient;
import dev.kad.invoicemanagement.model.entities.Client;
import dev.kad.invoicemanagement.model.entities.User;
import dev.kad.invoicemanagement.model.enums.Role;
import dev.kad.invoicemanagement.model.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "/users/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = userService.login(username, password, session);
            if (user.getRole() == Role.ADMIN) {
                return "redirect:/admin/dashboard";
            }
            return "/home";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/users/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "users/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model, HttpSession session) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }
        model.addAttribute("uc", new UserClient(new User(), new Client()));
        return "/users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("uc") UserClient uc,
                           BindingResult result,
                           HttpSession session,
                           Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }

        if (result.hasErrors()) {
            return "/users/register";
        }

        try {
            if (uc.getUser().getRole() == Role.USER) {
                userService.register(uc.getUser(), uc.getClient());
            } else {
                userService.register(uc.getUser());
            }
            model.addAttribute("successMessage", "Registration successful!");
            return "/admin/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/users/register";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, HttpSession session) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }
        model.addAttribute("user", userService.findUserById(id));
        return "users/edit-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "users/update";
        }

        try {
            userService.updateUser(id, user, session);
            model.addAttribute("successMessage", "User updated successfully!");
            return "/admin/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "users/update";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }

        try {
            userService.deleteUser(id, session);
            return "/admin/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "403";
        }
    }
}
