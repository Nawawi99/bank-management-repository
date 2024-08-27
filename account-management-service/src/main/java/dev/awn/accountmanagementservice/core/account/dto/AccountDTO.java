package dev.awn.accountmanagementservice.core.account.dto;

import dev.awn.accountmanagementservice.core.account.constant.AccountStatus;
import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountDTO {
    private Long id;
    private Long customerId;
    private BigDecimal balance;
    private AccountStatus status;
    private AccountType type;
}
