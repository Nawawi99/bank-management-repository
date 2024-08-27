package dev.awn.customermanagementservice.core.customer.service.impl;

import dev.awn.customermanagementservice.common.exception.BadRequestException;
import dev.awn.customermanagementservice.common.exception.ResourceNotFoundException;
import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.mapper.CustomerMapper;
import dev.awn.customermanagementservice.core.customer.model.Customer;
import dev.awn.customermanagementservice.core.customer.repository.CustomerRepository;
import dev.awn.customermanagementservice.core.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final long MINIMUM_ID_RANGE = 1_000_000;
    private final long MAXIMUM_ID_RANGE = 9_999_999;
    private final static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO getCustomer(long id) {
        logger.info("will be checking if id - {} is valid", id);
        if(id < MINIMUM_ID_RANGE || id >= MAXIMUM_ID_RANGE) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be getting the customer model of id - {}", id);
        Optional<Customer> customer = customerRepository.findById(id);

        if(customer.isEmpty()) {
            logger.warn("no customer was found of id - {}", id);
            throw new ResourceNotFoundException("no customer was found of id - " + id);
        }

        logger.info("a customer of id - {} was found, will be returning it", id);
        return customerMapper.toDto(customer.get());
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toModel(customerDTO);

        String legalId = customer.getLegalId();
        logger.info("will be checking if a customer already exists with legalId - {}", legalId);
        Optional<Customer> customerOptional = customerRepository.findByLegalId(legalId);
        if(customerOptional.isPresent()) {
            logger.warn("invalid legalId, customer already exists with this legalId - {}", legalId);
            throw new BadRequestException("invalid legalId, customer already exists with this legalId - " + legalId);
        }

        customer.setCreationTime(LocalDateTime.now());
        customer.setId(null);

        logger.info("will be saving newly created customer");
        customerRepository.save(customer);

        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO modifyCustomer(CustomerDTO customerDTO) {
        Long id = customerDTO.getId();

        logger.info("will be checking if id - {} is valid", customerDTO.getId());
        if(id == null || id < MINIMUM_ID_RANGE || id >= MAXIMUM_ID_RANGE) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be checking if a customer exists of id - {}", id);
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()) {
            logger.warn("no customer found of id - {}", id);
            throw new ResourceNotFoundException("no customer found of id - " + id);
        }

        String legalId = customerDTO.getLegalId();
        logger.info("will be checking if a customer exists of legalId - {}", legalId);
        customerOptional = customerRepository.findByLegalId(legalId);
        if(customerOptional.isPresent()) {
            long existingId = customerOptional.get().getId();
            logger.info("found customer of legalId - {}, will be checking if the legalId belongs to the same customer of id - {}",
                    legalId, existingId);

            if(existingId != id) {
                logger.warn("a customer already exists with legalId of - {}", legalId);
                throw new BadRequestException("a customer already exists with legalId of - " + legalId);
            }
        }

        Customer customer = customerMapper.toModel(customerDTO);
        customer.setModificationTime(LocalDateTime.now());

        logger.info("will be saving new customer");
        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public boolean removeCustomer(long id) {
        logger.info("will be checking if id - {} is valid", id);
        if(id < MINIMUM_ID_RANGE || id >= MAXIMUM_ID_RANGE) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be deleting customer of id - {}", id);
        customerRepository.deleteById(id);

        return true;
    }
}
