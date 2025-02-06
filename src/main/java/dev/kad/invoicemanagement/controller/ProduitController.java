package dev.kad.invoicemanagement.controller;

import dev.kad.invoicemanagement.model.entities.Produit;
import dev.kad.invoicemanagement.model.services.ProduitService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
@AllArgsConstructor
public class ProduitController {

    private ProduitService produitService;

    @GetMapping
    public String listProducts(
            @RequestParam(name = "filter", defaultValue = "ALL") String filter,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Produit> productsPage;
        if ("ALL".equals(filter)) {
            productsPage = produitService.getAllProducts(pageable);
        } else {
            productsPage = produitService.getProductsByType(filter, pageable);
        }
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("filter", filter);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());

        return "admin/products";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Produit());
        return "admin/edit-product";
    }

    @PostMapping("/save")
    public String saveProduit(@ModelAttribute("product") Produit product) {
        if (product.getId() != null && product.getId().isEmpty()) {
            product.setId(null);
        }
        produitService.saveProduit(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("product", produitService.getProduitById(id));
        return "admin/edit-product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduit(@PathVariable String id) {
        produitService.deleteProduit(id);
        return "redirect:/admin/products";
    }
}