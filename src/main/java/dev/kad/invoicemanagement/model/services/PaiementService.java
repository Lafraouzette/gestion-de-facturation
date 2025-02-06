package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Facture;
import dev.kad.invoicemanagement.model.entities.Paiement;
import dev.kad.invoicemanagement.model.enums.FactureStatus;
import dev.kad.invoicemanagement.model.enums.ModePaiement;
import dev.kad.invoicemanagement.model.repository.PaiementRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PaiementService {
    private final PaiementRepository paiementRepository;
    private final FactureService factureService;

    public Paiement createPaiement(Paiement paiement) {
        validPaiement(paiement);
        return paiementRepository.save(paiement);
    }

    public Paiement getPaiement(String id) {
        return paiementRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Paiement not found"));
    }

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    private void validPaiement(Paiement paiement) throws IllegalArgumentException {
        if (paiement.getMode() == ModePaiement.CHEQUE && (paiement.getBanque() == null || paiement.getChequeRef() == null)) {
            throw new IllegalArgumentException("Missing data for Paiement by CHEQUE");
        }
        if (paiement.getMontant() > 5000 && paiement.getMode() != ModePaiement.CHEQUE) {
            throw new IllegalArgumentException("Cannot pay more than 5000dh without CHEQUE");
        }
    }

    @Transactional
    public void makePayment(Long factureId, Paiement paiement) {

        Facture facture = factureService.getFacture(factureId);

        if (paiement.getMode() == ModePaiement.CHEQUE) {
            if (paiement.getBanque() == null || paiement.getChequeRef() == null || paiement.getOwner() == null) {
                throw new RuntimeException("Cheque details are required");
            }
        }

        paiement.setFacture(facture);
        paiementRepository.save(paiement);

        double totalPaid = facture.getPaiement().getMontant();

        if (totalPaid >= facture.getTotal()) {
            facture.setStatus(FactureStatus.PAYED);
        } else if (totalPaid > 0) {
            facture.setStatus(FactureStatus.PARTIALY_PAYED);
        }

        factureService.updateFacture(facture);
    }
}