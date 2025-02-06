package dev.kad.invoicemanagement.controller;

import dev.kad.invoicemanagement.model.entities.*;
import dev.kad.invoicemanagement.model.enums.CommandStatus;
import dev.kad.invoicemanagement.model.enums.Role;
import dev.kad.invoicemanagement.model.services.ClientService;
import dev.kad.invoicemanagement.model.services.CommandeService;
import dev.kad.invoicemanagement.model.services.FactureService;
import dev.kad.invoicemanagement.model.services.ProduitService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
public class AppController {
    private final ProduitService produitService;
    private final CommandeService commandeService;
    private final FactureService factureService;
    private final ClientService clientService;

    @GetMapping("/")
    public String rootURL(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser.getRole() == Role.ADMIN) {
                return "redirect:/admin/dashboard";
            } else if (loggedInUser.getRole() == Role.USER) {
                return "redirect:/home";
            }
        }
        return "/users/login";
    }

    @GetMapping("/home")
    public String home(
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "type", defaultValue = "ALL") String filterType,
            Model model) {

        // Fetch products based on filters
        List<Produit> products;
        if ("ALL".equals(filterType) && maxPrice == null) {
            products = produitService.getAllProduits();
        } else {
            products = produitService.getFilteredProducts(filterType, maxPrice); // Apply filters
        }

        Collections.shuffle(products);

        model.addAttribute("products", products);
        model.addAttribute("filterType", filterType);
        model.addAttribute("maxPrice", maxPrice);

        return "home";
    }

    @GetMapping("/product-details/{id}")
    public String productDetails(@PathVariable String id, Model model) {
        Produit product = produitService.getProduitById(id);
        model.addAttribute("product", product);
        return "users/product-details";
    }

    @GetMapping("/users/panier")
    public String viewPanier(Model model, HttpSession session) {
        List<Produit> panier = (List<Produit>) session.getAttribute("panier");
        if (panier == null) {
            panier = new ArrayList<>();
        }
        double total = panier.stream().mapToDouble(Produit::getPrice).sum();
        model.addAttribute("panier", panier);
        model.addAttribute("total", total);
        return "/users/panier";
    }

    @PostMapping("/users/panier/add")
    public String addToPanier(@RequestParam String productId, @RequestParam("quantite") Integer quantity, HttpSession session) {
        if (quantity == null || quantity < 1) {
            return "redirect:/home?error=Invalid quantity";
        }

        Produit product = produitService.getProduitById(productId);
        product.setQuantite(quantity.doubleValue());
        List<Produit> panier = (List<Produit>) session.getAttribute("panier");
        if (panier == null) {
            panier = new ArrayList<>();
        }

        for (int i = 0; i < quantity; i++) {
            panier.add(product);
        }

        session.setAttribute("panier", panier);
        return "redirect:/users/panier";
    }

    @GetMapping("/users/paiement")
    public String paiement(Model model) {
        model.addAttribute("mode", "ESPECE");
        return "/users/paiement";
    }

    @GetMapping("/factures")
    public String factures(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        List<Facture> factures;
        if (loggedInUser.getRole() == Role.ADMIN) {
            factures = factureService.getAllFactures();
        } else if (loggedInUser.getRole() == Role.USER) {
            factures = factureService.getFacturesByClientId(loggedInUser.getClientId());
        } else {
            return "redirect:403";
        }

        model.addAttribute("factures", factures);
        return "factures";
    }

    @PostMapping("/users/paiement")
    public String processPaiement(@ModelAttribute Paiement paiement, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        if( loggedInUser.getRole() == Role.USER) {
            List<Produit> panier = (List<Produit>) session.getAttribute("panier");
            double total = panier.stream().mapToDouble(Produit::getPrice).sum();

            if (paiement.getMontant() > total) {
                throw new RuntimeException("Payment amount exceeds total");
            }

            Commande commande = new Commande();
            commande.setProduits(panier);
            commande.setClient(clientService.getClientById(loggedInUser.getClientId()));
            commande.setStatus(CommandStatus.PENDING);
            commandeService.createCommande(commande);
            factureService.generateFacture(commande, paiement);

            session.removeAttribute("panier");
        }


        return "redirect:/factures";
    }
}