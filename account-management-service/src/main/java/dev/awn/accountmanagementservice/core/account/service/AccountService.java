package dev.awn.accountmanagementservice.core.account.service;

import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AccountService {
    AccountDTO getAccount(long id);

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO modifyAccount(AccountDTO accountDTO);

    boolean removeAccount(long id);
}
