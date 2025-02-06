package dev.kad.invoicemanagement.model.repository;

import dev.kad.invoicemanagement.model.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, String> {
}
