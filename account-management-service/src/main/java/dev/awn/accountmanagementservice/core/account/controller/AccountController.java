package dev.awn.accountmanagementservice.core.account.controller;

import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        return null;
    }

    @PutMapping
    public ResponseEntity<AccountDTO> modifyAccount(@RequestBody AccountDTO accountDTO) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAccount(@PathVariable Long id) {
        return null;
    }
}
