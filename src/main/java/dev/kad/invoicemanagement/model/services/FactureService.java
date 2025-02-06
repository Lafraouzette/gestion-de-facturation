package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Commande;
import dev.kad.invoicemanagement.model.entities.Facture;
import dev.kad.invoicemanagement.model.entities.Paiement;
import dev.kad.invoicemanagement.model.entities.Produit;
import dev.kad.invoicemanagement.model.enums.FactureStatus;
import dev.kad.invoicemanagement.model.repository.FactureRepository;
import dev.kad.invoicemanagement.model.repository.PaiementRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class FactureService {
    private final FactureRepository factureRepository;
    private final PdfService pdfService;
    private final PaiementRepository paiementRepository;

    public Facture createFacture(Facture facture) {
        return factureRepository.save(facture);
    }

    public Facture updateFacture(Facture facture) {
        getFacture(facture.getId());
        return factureRepository.save(facture);
    }

    public Facture getFacture(Long id) {
        return factureRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Facture not found"));
    }

    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    public List<Facture> getFacturesByClientId(String clientId) {
        return factureRepository.findByCommande_Client_Id(clientId);
    }

    @Transactional
    public void generateFacture(Commande commande) {
        double total = commande.getProduits().stream()
                .mapToDouble(Produit::getPrice)
                .sum();

        String reference = generateFactureReference();

        Facture facture = new Facture();
        facture.setDate(LocalDate.now().toString());
        facture.setTotal(total);
        facture.setStatus(FactureStatus.UNPAYED);
        facture.setCommande(commande);
        facture.setReference(reference);

        factureRepository.save(facture);
    }

    public Facture generateFacture(Commande commande, Paiement paiement) {
        Paiement save1 = paiementRepository.save(paiement);
        Facture facture = new Facture();
        facture.setCommande(commande);
        facture.setPaiement(save1);
        facture.setReference("REF-" + System.currentTimeMillis()); // Example reference
        facture.setDate(java.time.LocalDate.now().toString());
        facture.setTotal(commande.getProduits().stream().mapToDouble(Produit::getPrice).sum());
        facture.setStatus(FactureStatus.PAYED);
        Facture save = this.factureRepository.save(facture);
        save1.setFacture(save);
        paiementRepository.save(save1);
        try {
            pdfService.generateFacturePdf(save, "C:\\Users\\Hamza\\Desktop\\RSI S3\\facture.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return save;
    }

    private String generateFactureReference() {
        Facture lastFacture = factureRepository.findTopByOrderByIdDesc();
        if (lastFacture == null) {
            return "FACT-0001";
        } else {
            String lastRef = lastFacture.getReference();
            int nextNumber = Integer.parseInt(lastRef.split("-")[1]) + 1;
            return String.format("FACT-%04d", nextNumber);
        }
    }

    @Transactional
    public void modifyFacture(Long factureId, Facture updatedFacture) {
        Facture originalFacture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture not found"));

        Facture newFacture = new Facture();
        newFacture.setDate(LocalDate.now().toString());
        newFacture.setTotal(updatedFacture.getTotal());
        newFacture.setStatus(updatedFacture.getStatus());
        newFacture.setCommande(originalFacture.getCommande());
        newFacture.setReference(generateFactureReference());
        newFacture.setIdParent(originalFacture.getReference());

        factureRepository.save(newFacture);
    }
}