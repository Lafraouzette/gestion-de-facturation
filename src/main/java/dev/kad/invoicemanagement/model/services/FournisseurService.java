package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Fournisseur;
import dev.kad.invoicemanagement.model.repository.FournisseurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    public Fournisseur save(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public Fournisseur findById(String id) {
        return fournisseurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fournisseur not found with id: " + id));
    }

    public List<Fournisseur> findAll() {
        return fournisseurRepository.findAll();
    }

    public void deleteById(String id) {
        fournisseurRepository.deleteById(id);
    }
}
