package dev.awn.accountmanagementservice.core.account.service.impl;

import dev.awn.accountmanagementservice.common.exception.BadRequestException;
import dev.awn.accountmanagementservice.common.exception.ResourceNotFoundException;
import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.dto.CustomerDTO;
import dev.awn.accountmanagementservice.core.account.mapper.AccountMapper;
import dev.awn.accountmanagementservice.core.account.model.Account;
import dev.awn.accountmanagementservice.core.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountDTO accountDTO;
    private CustomerDTO customerDTO;

    private final long VALID_ACCOUNT_ID = 1_000_000_001L;
    private final long INVALID_ACCOUNT_ID = 999L;
    private final long CUSTOMER_ID = 1_000_000L;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(VALID_ACCOUNT_ID);
        account.setCustomerId(CUSTOMER_ID);

        accountDTO = new AccountDTO();
        accountDTO.setId(VALID_ACCOUNT_ID);
        accountDTO.setCustomerId(CUSTOMER_ID);

        customerDTO = new CustomerDTO();
        customerDTO.setId(CUSTOMER_ID);
    }

    @Test
    void testGetAccount_WhenValidId_ReturnsAccount() {
        // Arrange
        when(accountRepository.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDTO);

        // Act
        AccountDTO result = accountService.getAccount(VALID_ACCOUNT_ID);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_ACCOUNT_ID, result.getId());
        verify(accountRepository, times(1)).findById(VALID_ACCOUNT_ID);
    }

    @Test
    void testGetAccount_WhenInvalidId_ThrowsBadRequestException() {
        // Arrange

        // Act & Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            accountService.getAccount(INVALID_ACCOUNT_ID);
        });

        assertEquals("invalid id - " + INVALID_ACCOUNT_ID, exception.getMessage());
    }

    @Test
    void testGetAccount_WhenAccountDoesntExist_ThrowsResourceNotFoundException() {
        // Arrange
        when(accountRepository.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccount(VALID_ACCOUNT_ID);
        });

        assertEquals("no account was found of id - " + VALID_ACCOUNT_ID, exception.getMessage());
    }

    @Test
    void testCreateAccount_WhenValidCustomer_ReturnsAccount() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(CustomerDTO.class))).thenReturn(customerDTO);
        when(accountRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(new ArrayList<>());
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toModel(any(AccountDTO.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);
        accountDTO.setType(AccountType.SAVINGS);

        // Act
        AccountDTO result = accountService.createAccount(accountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_ACCOUNT_ID, result.getId());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_WhenCustomerNotFound_ThrowsBadRequestException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(CustomerDTO.class))).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            accountService.createAccount(accountDTO);
        });

        assertEquals("no customer found of customerId - " + CUSTOMER_ID, exception.getMessage());
    }

    @Test
    void testCreateAccount_WhenMaximumAccountsExceeded_ThrowsBadRequestException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(CustomerDTO.class))).thenReturn(customerDTO);
        List<Account> existingAccounts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            existingAccounts.add(new Account());
        }
        when(accountRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(existingAccounts);

        // Act & Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            accountService.createAccount(accountDTO);
        });

        assertEquals("customerId of - " + CUSTOMER_ID + " already has maximum number of accounts allowed", exception.getMessage());
    }

    @Test
    void testCreateAccount_WhenSalaryAccountExists_ThrowsBadRequestException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(CustomerDTO.class))).thenReturn(customerDTO);
        accountDTO.setType(AccountType.SALARY);
        when(accountRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(new ArrayList<>());
        when(accountRepository.findByCustomerIdAndType(CUSTOMER_ID, AccountType.SALARY)).thenReturn(Optional.of(account));

        // Act & Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            accountService.createAccount(accountDTO);
        });

        assertEquals("customer of customerId " + CUSTOMER_ID + " already has a salary account", exception.getMessage());
    }

    @Test
    void testModifyAccount_WhenValidModification_ReturnsAccount() {
        // Arrange
        accountDTO.setType(AccountType.SAVINGS);
        when(accountRepository.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toModel(any(AccountDTO.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);

        // Act
        AccountDTO result = accountService.modifyAccount(accountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_ACCOUNT_ID, result.getId());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testModifyAccount_WhenInvalidId_ThrowsBadRequestException() {
        // Arrange
        accountDTO.setId(INVALID_ACCOUNT_ID);

        // Act & Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            accountService.modifyAccount(accountDTO);
        });

        assertEquals("invalid id - " + INVALID_ACCOUNT_ID, exception.getMessage());
    }

    @Test
    void testModifyAccount_WhenSalaryAccountAlreadyExists_ThrowsBadRequestException() {
        // Arrange
        accountDTO.setType(AccountType.SALARY);
        Account anotherSalaryAccount = new Account();
        anotherSalaryAccount.setId(VALID_ACCOUNT_ID + 1);
        anotherSalaryAccount.setCustomerId(CUSTOMER_ID);
        anotherSalaryAccount.setType(AccountType.SALARY);

        when(accountRepository.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(accountRepository.findByCustomerIdAndType(CUSTOMER_ID, AccountType.SALARY)).thenReturn(Optional.of(anotherSalaryAccount));

        // Act & Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            accountService.modifyAccount(accountDTO);
        });

        assertEquals("only one salary account is allowed", exception.getMessage());
    }

    @Test
    void testRemoveAccount_WhenValidId_RemovesAccount() {
        // Arrange

        // Act
        boolean result = accountService.removeAccount(VALID_ACCOUNT_ID);

        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).deleteById(VALID_ACCOUNT_ID);
    }

    @Test
    void testRemoveAccount_WhenInvalidId_ThrowsBadRequestException() {
        // Arrange

        // Act & Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            accountService.removeAccount(INVALID_ACCOUNT_ID);
        });

        assertEquals("invalid id - " + INVALID_ACCOUNT_ID, exception.getMessage());
    }

}

