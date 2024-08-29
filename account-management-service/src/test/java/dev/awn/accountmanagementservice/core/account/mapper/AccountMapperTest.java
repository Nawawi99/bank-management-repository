package dev.awn.accountmanagementservice.core.account.mapper;

import dev.awn.accountmanagementservice.core.account.constant.AccountStatus;
import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.model.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountMapperTest {

    private AccountMapper accountMapper = new AccountMapper();

    @Test
    void testToModel_WhenAccountDTOIsValid_ReturnsAccount() {
        // Arrange
        AccountDTO accountDTO = AccountDTO.builder()
                                          .id(1_000_001_001L)
                                          .balance(BigDecimal.valueOf(1000.00))
                                          .type(AccountType.SAVINGS)
                                          .status(AccountStatus.ACTIVE)
                                          .customerId(1_000_001L)
                                          .build();

        // Act
        Account account = accountMapper.toModel(accountDTO);

        // Assert
        assertNotNull(account);
        assertEquals(accountDTO.getId(), account.getId());
        assertEquals(accountDTO.getBalance(), account.getBalance());
        assertEquals(accountDTO.getType(), account.getType());
        assertEquals(accountDTO.getStatus(), account.getStatus());
        assertEquals(accountDTO.getCustomerId(), account.getCustomerId());
    }

    @Test
    void testToDto_WhenAccountIsValid_ReturnsAccountDTO() {
        // Arrange
        Account account = Account.builder()
                                 .id(1_000_001_001L)
                                 .balance(BigDecimal.valueOf(2000.00))
                                 .type(AccountType.INVESTMENT)
                                 .status(AccountStatus.INACTIVE)
                                 .customerId(1_000_002L)
                                 .build();

        // Act
        AccountDTO accountDTO = accountMapper.toDto(account);

        // Assert
        assertNotNull(accountDTO);
        assertEquals(account.getId(), accountDTO.getId());
        assertEquals(account.getBalance(), accountDTO.getBalance());
        assertEquals(account.getType(), accountDTO.getType());
        assertEquals(account.getStatus(), accountDTO.getStatus());
        assertEquals(account.getCustomerId(), accountDTO.getCustomerId());
    }
}

