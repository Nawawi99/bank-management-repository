package dev.awn.accountmanagementservice.core.account.service.impl;

import dev.awn.accountmanagementservice.common.exception.BadRequestException;
import dev.awn.accountmanagementservice.common.exception.ResourceNotFoundException;
import dev.awn.accountmanagementservice.core.account.constant.AccountType;
import dev.awn.accountmanagementservice.core.account.dto.AccountDTO;
import dev.awn.accountmanagementservice.core.account.dto.CustomerDTO;
import dev.awn.accountmanagementservice.core.account.mapper.AccountMapper;
import dev.awn.accountmanagementservice.core.account.model.Account;
import dev.awn.accountmanagementservice.core.account.repository.AccountRepository;
import dev.awn.accountmanagementservice.core.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final long MAXIMUM_ACCOUNT_ID_RANGE = 9_999_999_999L;
    private final long MINIMUM_ACCOUNT_ID_RANGE = 1_000_000_001L;

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final RestTemplate restTemplate;

    @Override
    public AccountDTO getAccount(long id) {
        logger.info("will be checking if id - {} is valid", id);
        if(id > MAXIMUM_ACCOUNT_ID_RANGE || id < MINIMUM_ACCOUNT_ID_RANGE) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be getting the customer of id - {}", id);
        Optional<Account> account = accountRepository.findById(id);

        if(account.isEmpty()) {
            logger.warn("no account was found of id - {}", id);
            throw new ResourceNotFoundException("no account was found of id - " + id);
        }

        logger.info("an account of id - {} was found, will be returning it", id);
        return accountMapper.toDto(account.get());
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        long customerId = accountDTO.getCustomerId();

        logger.info("will be checking if customer exists of customerId - {}", customerId);
        CustomerDTO customer = getCustomerById(customerId);
        if (customer == null) {
            logger.warn("no customer found of customerId - {}", customerId);
            throw new BadRequestException("no customer found of customerId - " + customerId);
        }

        logger.info("retrieving accounts for customerId - {}", customerId);
        List<Account> existingAccounts = accountRepository.findByCustomerId(customerId);

        if (existingAccounts.size() >= 10) {
            logger.warn("customerId of - {} already has maximum number of accounts allowed", customerId);
            throw new BadRequestException("customerId of - " + customerId + " already has maximum number of accounts allowed");
        }

        logger.info("will perform account type checking");
        if(accountDTO.getType().equals(AccountType.SALARY)) {
            logger.info("account type is SALARY, will be searching for account where customerId is {} and type is SALARY", customerId);
            Optional<Account> accountOptional = accountRepository.findByCustomerIdAndType(customerId, AccountType.SALARY);
            if(accountOptional.isPresent()) {
                logger.warn("found a SALARY account of id - {}, will not be creating the acount", accountOptional.get().getId());
                throw new BadRequestException("customer of customerId " + customerId + " already has a salary account");
            }
        }

        logger.info("will construct the account id suffixes (last 3 digits)");
        Set<Integer> existingAccountSuffixes = existingAccounts.stream()
                                                               .map(account -> (int) (account.getId() % 1000))
                                                               .collect(Collectors.toSet());
        logger.info("found the following suffixes -> {}", existingAccountSuffixes);

        logger.info("will attempt to generate a new suffix");
        int newSuffix = -1;
        for (int i = 1; i <= 10; i++) {
            if (!existingAccountSuffixes.contains(i)) {
                newSuffix = i;
                logger.info("found new suffix - {}", newSuffix);
                break;
            }
        }

        long newAccountId = customerId * 1000 + newSuffix;
        logger.info("calculated new account id, produced the following - {}", newAccountId);
        accountDTO.setId(newAccountId);

        Account account = accountMapper.toModel(accountDTO);
        account.setCreationTime(LocalDateTime.now());

        logger.info("will be saving the new account");
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDto(savedAccount);
    }

    @Override
    public AccountDTO modifyAccount(AccountDTO accountDTO) {
        Long id = accountDTO.getId();

        logger.info("will be checking if id - {} is valid", id);
        if(id == null || id > MAXIMUM_ACCOUNT_ID_RANGE || id < MINIMUM_ACCOUNT_ID_RANGE) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be checking if an account exists of id - {}", id);
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(accountOptional.isEmpty()) {
            logger.warn("no account found of id - {}", id);
            throw new ResourceNotFoundException("no account found of id - " + id);
        }

        AccountType accountType = accountDTO.getType();

        if(accountType.equals(AccountType.SALARY)) {
            logger.info("will be checking if a salary account already exists");

            long customerId = accountDTO.getCustomerId();
            accountOptional = accountRepository.findByCustomerIdAndType(
                    customerId, accountType
            );

            if(accountOptional.isPresent()) {
                logger.info("found a salary account for customerId - {} with the id - {}", customerId, id);

                logger.info("will be checking if the account being updated is the salary account itself");
                long existingId = accountOptional.get().getId();
                if(id != existingId) {
                    logger.warn("customer of id - {} already contains a salary account of id - {}", customerId, existingId);
                    throw new BadRequestException("only one salary account is allowed");
                }
            }
        }

        Account account = accountMapper.toModel(accountDTO);
        account.setModificationTime(LocalDateTime.now());

        logger.info("will be saving modified account");
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toDto(savedAccount);
    }

    @Override
    public boolean removeAccount(long id) {
        logger.info("will be checking if id - {} is valid", id);
        if(id < MINIMUM_ACCOUNT_ID_RANGE || id > MAXIMUM_ACCOUNT_ID_RANGE) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be deleting account of id - {}", id);
        accountRepository.deleteById(id);

        return true;
    }

    @KafkaListener(topics = "account-cleanup-topic",
            groupId = "account-cleanup-topic-consumer-group")
    @Transactional
    public void removeAccountsBy(String customerId) {
        logger.info("received an event where customer of id - {} was deleted", customerId);

        logger.info("will be deleting all accounts under customer id of - {}", customerId);
        int accountsDeleted = accountRepository.deleteAllByCustomerId(Long.parseLong(customerId));

        logger.info("removed {} account/s of customer id - {}", accountsDeleted, customerId);
    }

    private CustomerDTO getCustomerById(long customerId) {
        final String CUSTOMER_SERVICE_URL = "http://localhost:8081/api/v1/customers/";

        try {
            String url = CUSTOMER_SERVICE_URL + customerId;
            logger.info("will be hitting the following url to get customer details - {}", url);
            return restTemplate.getForObject(url, CustomerDTO.class);
        } catch (RestClientException e) {
            logger.error("error fetching customer of id - {}", customerId, e);
            throw new BadRequestException("customer service is unavailable or customer does not exist.");
        }
    }
}
