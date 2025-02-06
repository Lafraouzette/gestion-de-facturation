package dev.kad.invoicemanagement.model.entities;

import dev.kad.invoicemanagement.model.enums.ModePaiement;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Paiement {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String owner;
    @Nullable
    private String banque;
    @Nullable
    private String chequeRef;
    private double montant;
    @Enumerated(value = EnumType.STRING)
    private ModePaiement mode;
    @OneToOne(mappedBy = "paiement")
    private Facture facture;
}
