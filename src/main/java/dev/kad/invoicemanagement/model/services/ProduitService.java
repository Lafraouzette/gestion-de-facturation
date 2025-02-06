package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Fournisseur;
import dev.kad.invoicemanagement.model.entities.Produit;
import dev.kad.invoicemanagement.model.enums.ProductsType;
import dev.kad.invoicemanagement.model.repository.ProduitRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProduitService {
    private final ProduitRepository produitRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Page<Produit> getAllProducts(Pageable pageable) {
        return produitRepository.findAll(pageable);
    }

    public Page<Produit> getProductsByType(String type, Pageable pageable) {
        return produitRepository.findByType(ProductsType.valueOf(type), pageable);
    }

    public Produit getProduitById(String id) {
        return produitRepository.findById(id).orElse(null);
    }

    public Produit saveProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public void deleteProduit(String id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> getFilteredProducts(String type, Double maxPrice) {
        if ("ALL".equals(type) && maxPrice == null) {
            return produitRepository.findAll();
        } else if ("ALL".equals(type)) {
            return produitRepository.findByPriceLessThanEqual(maxPrice);
        } else if (maxPrice == null) {
            return produitRepository.findByType(ProductsType.valueOf(type));
        } else {
            return produitRepository.findByTypeAndPriceLessThanEqual(ProductsType.valueOf(type), maxPrice);
        }
    }

}
