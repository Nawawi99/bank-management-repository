package dev.awn.accountmanagementservice.core.account.repository;

import dev.awn.accountmanagementservice.core.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
// 9_999_999_010
}
