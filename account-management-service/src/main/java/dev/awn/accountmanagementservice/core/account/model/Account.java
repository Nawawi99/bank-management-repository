package dev.awn.accountmanagementservice.core.account.model;

import dev.awn.accountmanagementservice.core.account.constant.AccountStatus;
import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNTS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Account {
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private AccountType type;
}
