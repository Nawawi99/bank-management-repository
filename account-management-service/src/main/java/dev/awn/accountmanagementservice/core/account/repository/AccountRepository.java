package dev.awn.accountmanagementservice.core.account.repository;

import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import dev.awn.accountmanagementservice.core.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCustomerIdAndType(long customerId, AccountType accountType);

    List<Account> findByCustomerId(long customerId);
}
