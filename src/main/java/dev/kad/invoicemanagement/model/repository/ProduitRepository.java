package dev.kad.invoicemanagement.model.repository;

import dev.kad.invoicemanagement.model.entities.Produit;
import dev.kad.invoicemanagement.model.enums.ProductsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, String> {
    Page<Produit> findByType(ProductsType productsType, Pageable pageable);
    List<Produit> findByType(ProductsType productsType);
    List<Produit> findByPriceLessThanEqual(Double maxPrice);
    List<Produit> findByTypeAndPriceLessThanEqual(ProductsType type, Double maxPrice);
}
