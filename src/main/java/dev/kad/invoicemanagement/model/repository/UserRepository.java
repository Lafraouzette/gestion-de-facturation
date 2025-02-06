package dev.kad.invoicemanagement.model.repository;

import dev.kad.invoicemanagement.model.entities.User;
import dev.kad.invoicemanagement.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String admin);
}
