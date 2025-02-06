package dev.kad.invoicemanagement.model.entities;

import dev.kad.invoicemanagement.model.enums.CommandStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Commande {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Date date;
    @Enumerated(value = EnumType.STRING)
    private CommandStatus status;
    @ManyToOne
    private Client client;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commande")
    private Collection<Produit> produits = new ArrayList<>();
}
