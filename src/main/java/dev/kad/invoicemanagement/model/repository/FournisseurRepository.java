package dev.kad.invoicemanagement.model.repository;

import dev.kad.invoicemanagement.model.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, String> {
}
