package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Commande;
import dev.kad.invoicemanagement.model.enums.CommandStatus;
import dev.kad.invoicemanagement.model.repository.CommandeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final FactureService factureService;

    public Commande createCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    public Commande updateCommande(Commande commande) {
        this.getCommande(commande.getId());
        return commandeRepository.save(commande);
    }

    public void deleteCommande(String id) {
        commandeRepository.delete(getCommande(id));
    }

    public Commande getCommande(String id) {
        return commandeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Commande not found"));
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public List<Commande> getPendingCommandes() {
        return commandeRepository.findByStatus(CommandStatus.PENDING);
    }

    @Transactional
    public void confirmCommande(String commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        commande.setStatus(CommandStatus.CONFIRMED);
        commandeRepository.save(commande);

        factureService.generateFacture(commande);
    }
}
