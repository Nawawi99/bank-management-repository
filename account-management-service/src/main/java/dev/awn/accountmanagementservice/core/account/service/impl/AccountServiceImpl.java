package dev.awn.accountmanagementservice.core.account.service.impl;

import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.repository.AccountRepository;
import dev.awn.accountmanagementservice.core.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public AccountDTO getAccount(Long id) {
        return null;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        return null;
    }

    @Override
    public AccountDTO modifyAccount(AccountDTO accountDTO) {
        return null;
    }

    @Override
    public boolean removeAccount(Long id) {
        return false;
    }
}
