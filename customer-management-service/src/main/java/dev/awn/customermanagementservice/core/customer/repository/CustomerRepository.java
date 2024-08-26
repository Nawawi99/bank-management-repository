package dev.awn.customermanagementservice.core.customer.repository;

import dev.awn.customermanagementservice.core.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByLegalId(String legalId);
}
