package dev.awn.accountmanagementservice.core.account.controller;

import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.getAccount(id);

        return ResponseEntity.status(HttpStatus.OK).body(accountDTO);
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping
    public ResponseEntity<AccountDTO> modifyAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO modifiedAccount = accountService.modifyAccount(accountDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(modifiedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAccount(@PathVariable Long id) {
        accountService.removeAccount(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
