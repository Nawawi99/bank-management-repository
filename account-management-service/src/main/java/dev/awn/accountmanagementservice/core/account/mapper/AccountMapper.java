package dev.awn.accountmanagementservice.core.account.mapper;

import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toModel(AccountDTO accountDTO) {
        return Account.builder()
                      .id(accountDTO.getId())
                      .balance(accountDTO.getBalance())
                      .type(accountDTO.getType())
                      .status(accountDTO.getStatus())
                      .customerId(accountDTO.getCustomerId())
                      .build();
    }

    public AccountDTO toDto(Account account) {
        return AccountDTO.builder()
                         .id(account.getId())
                         .balance(account.getBalance())
                         .type(account.getType())
                         .status(account.getStatus())
                         .customerId(account.getCustomerId())
                         .build();
    }

}
