package dev.kad.invoicemanagement.controller;

import dev.kad.invoicemanagement.model.entities.Commande;
import dev.kad.invoicemanagement.model.entities.User;
import dev.kad.invoicemanagement.model.enums.Role;
import dev.kad.invoicemanagement.model.services.ClientService;
import dev.kad.invoicemanagement.model.services.CommandeService;
import dev.kad.invoicemanagement.model.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ClientService clientService;
    private final CommandeService commandeService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || ((User) loggedInUser).getRole() != Role.ADMIN) {
            return "403";
        }
        model.addAttribute("stats", "userService.getDashboardStats()");
        return "/admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(HttpSession session, Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/clients")
    public String manageClients(HttpSession session, Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }
        model.addAttribute("clients", clientService.getAllClients());
        return "admin/clients";
    }

    @PostMapping("/delete/user/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }
        userService.deleteUser(id, session);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/client/{id}")
    public String deleteClient(@PathVariable String id, HttpSession session, Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }
        clientService.deleteClient(id);
        return "redirect:/admin/clients";
    }

    @GetMapping("/commandes")
    public String manageCommandes(HttpSession session, Model model) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }

        List<Commande> pendingCommandes = commandeService.getPendingCommandes();
        model.addAttribute("commandes", pendingCommandes);

        return "/admin/commandes";
    }

    @PostMapping("/commandes/confirm/{id}")
    public String confirmCommande(@PathVariable String id, HttpSession session) {
        User loggedInUser = userService.getSessionUser(session);
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN) {
            return "403";
        }

        commandeService.confirmCommande(id);

        return "redirect:/admin/commandes";
    }
}

