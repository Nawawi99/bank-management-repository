package dev.awn.accountmanagementservice.core.account.dto;

import dev.awn.accountmanagementservice.core.account.constant.AccountStatus;
import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountDTO {
    private Long id;
    @NotNull(message = "customerId cannot be empty")
    private Long customerId;
    @NotNull(message = "balance cannot be empty")
    private BigDecimal balance;
    @NotNull(message = "status cannot be empty")
    private AccountStatus status;
    @NotNull(message = "type cannot be empty")
    private AccountType type;
}
