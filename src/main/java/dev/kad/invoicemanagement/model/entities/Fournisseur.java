package dev.kad.invoicemanagement.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Fournisseur {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    @ManyToMany
    @JoinTable(
            name = "fournisseur_produits",
            joinColumns = @JoinColumn(name = "fournisseur_id"),
            inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    private Collection<Produit> produits;

}