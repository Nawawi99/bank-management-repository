package dev.awn.accountmanagementservice.core.account.controller;

import dev.awn.accountmanagementservice.core.account.constant.AccountStatus;
import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private static final long ACCOUNT_ID = 1_000_000_001L;
    private static final long CUSTOMER_ID = 1_000_000L;

    @Test
    void testGetAccount_WhenAccountExists_ReturnsAccountDTO() throws Exception {
        // arrange
        AccountDTO accountDTO = AccountDTO.builder()
                                          .id(ACCOUNT_ID)
                                          .customerId(CUSTOMER_ID)
                                          .balance(BigDecimal.valueOf(1000))
                                          .status(AccountStatus.ACTIVE)
                                          .type(AccountType.SAVINGS)
                                          .build();

        when(accountService.getAccount(ACCOUNT_ID)).thenReturn(accountDTO);

        // act
        mockMvc.perform(get("/accounts/{id}", ACCOUNT_ID))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(ACCOUNT_ID))
               .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
               .andExpect(jsonPath("$.balance").value(1000))
               .andExpect(jsonPath("$.status").value(AccountStatus.ACTIVE.toString()))
               .andExpect(jsonPath("$.type").value(AccountType.SAVINGS.toString()));

        // assert
        verify(accountService).getAccount(ACCOUNT_ID);
    }

    @Test
    void testCreateAccount_WhenAccountIsValid_ReturnsCreatedAccountDTO() throws Exception {
        // arrange
        AccountDTO accountDTO = AccountDTO.builder()
                                          .id(ACCOUNT_ID)
                                          .customerId(CUSTOMER_ID)
                                          .balance(BigDecimal.valueOf(2000))
                                          .status(AccountStatus.ACTIVE)
                                          .type(AccountType.SAVINGS)
                                          .build();

        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(accountDTO);

        // act
        mockMvc.perform(post("/accounts")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"id\":null,\"customerId\":1000000,\"balance\":2000,\"status\":\"ACTIVE\",\"type\":\"SAVINGS\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(ACCOUNT_ID))
               .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
               .andExpect(jsonPath("$.balance").value(2000))
               .andExpect(jsonPath("$.status").value(AccountStatus.ACTIVE.toString()))
               .andExpect(jsonPath("$.type").value(AccountType.SAVINGS.toString()));

        // assert
        verify(accountService).createAccount(any(AccountDTO.class));
    }

    @Test
    void testModifyAccount_WhenAccountIsValid_ReturnsModifiedAccountDTO() throws Exception {
        // arrange
        AccountDTO modifiedAccountDTO = AccountDTO.builder()
                                                  .id(ACCOUNT_ID)
                                                  .customerId(CUSTOMER_ID)
                                                  .balance(BigDecimal.valueOf(3000))
                                                  .status(AccountStatus.INACTIVE)
                                                  .type(AccountType.SALARY)
                                                  .build();

        when(accountService.modifyAccount(any(AccountDTO.class))).thenReturn(modifiedAccountDTO);

        // act
        mockMvc.perform(put("/accounts")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"id\":1000000001,\"customerId\":1000000,\"balance\":3000,\"status\":\"INACTIVE\",\"type\":\"SALARY\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(ACCOUNT_ID))
               .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
               .andExpect(jsonPath("$.balance").value(3000))
               .andExpect(jsonPath("$.status").value(AccountStatus.INACTIVE.toString()))
               .andExpect(jsonPath("$.type").value(AccountType.SALARY.toString()));

        // assert
        verify(accountService).modifyAccount(any(AccountDTO.class));
    }

    @Test
    void testRemoveAccount_WhenAccountExists_ReturnsOk() throws Exception {
        // act
        mockMvc.perform(delete("/accounts/{id}", ACCOUNT_ID))
               .andExpect(status().isOk());

        // assert
        verify(accountService).removeAccount(ACCOUNT_ID);
    }
}

