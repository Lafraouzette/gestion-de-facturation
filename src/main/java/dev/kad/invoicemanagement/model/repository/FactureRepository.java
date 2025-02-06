package dev.kad.invoicemanagement.model.repository;

import dev.kad.invoicemanagement.model.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    Facture findTopByOrderByIdDesc();

    List<Facture> findByCommande_Client_Id(String clientId);
}
