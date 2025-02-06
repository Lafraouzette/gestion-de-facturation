package dev.kad.invoicemanagement.model.entities;

import dev.kad.invoicemanagement.model.enums.ProductsType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Produit {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private double price;
    private double tva;
    private double quantiteStock;
    private double quantite;
    @Enumerated(value = EnumType.STRING)
    private ProductsType type;
    @ManyToMany(mappedBy = "produits", fetch = FetchType.EAGER)
    private Collection<Fournisseur> fournisseurs;
    @ManyToOne
    private Commande commande;
}