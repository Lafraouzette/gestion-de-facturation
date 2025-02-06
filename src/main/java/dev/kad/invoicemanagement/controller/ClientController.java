package dev.kad.invoicemanagement.controller;

import dev.kad.invoicemanagement.model.entities.Client;
import dev.kad.invoicemanagement.model.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        Client client = clientService.getClientById(id);
        model.addAttribute("client", client);
        return "/users/edit-client";
    }

    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable String id, @ModelAttribute Client client) {
        clientService.updateClient(id, client);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return "redirect:/admin/dashboard";
    }
}
