package dev.kad.invoicemanagement.model.repository;

import dev.kad.invoicemanagement.model.entities.Commande;
import dev.kad.invoicemanagement.model.enums.CommandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, String> {
    List<Commande> findByStatus(CommandStatus commandStatus);
}
