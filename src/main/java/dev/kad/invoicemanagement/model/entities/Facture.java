package dev.kad.invoicemanagement.model.entities;

import dev.kad.invoicemanagement.model.enums.FactureStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Facture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private String idParent;
    private String reference;
    private String date;
    private double total;
    @Enumerated(value = EnumType.STRING)
    private FactureStatus status;
    @OneToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
    @OneToOne
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;
}
