package dev.awn.customermanagementservice.core.customer.service.impl;

import dev.awn.customermanagementservice.common.exception.BadRequestException;
import dev.awn.customermanagementservice.common.exception.ResourceNotFoundException;
import dev.awn.customermanagementservice.core.customer.constant.CustomerType;
import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.mapper.CustomerMapper;
import dev.awn.customermanagementservice.core.customer.model.Customer;
import dev.awn.customermanagementservice.core.customer.repository.CustomerRepository;
import dev.awn.customermanagementservice.core.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    @Value("${property.minimum-id}")
    private long minimumIdRange;
    private final static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO getCustomer(long id) {
        logger.info("will be checking if id - {} is valid", id);
        if(id < minimumIdRange) {
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

        logger.info("will be checking if id is empty");
        if(customer.getId() != null) {
            logger.warn("newly created customer cannot contain a pre-generated id - {}", customerDTO.getId());
            throw new BadRequestException("newly created customer cannot contain a pre-generated id - " + customerDTO.getId());
        }

        logger.info("will be checking if customer type is valid");
        if(!CustomerType.isValid(customerDTO.getType().toString())) {
            logger.warn("customer type {} is invalid or undefined", customerDTO.getType().toString());
            throw new BadRequestException("customer type " + customerDTO.getType().toString() + " is invalid or undefined");
        }

        String legalId = customer.getLegalId();
        logger.info("will be checking if a customer already exists with legalId - {}", legalId);
        Optional<Customer> customerOptional = customerRepository.findByLegalId(legalId);
        if(customerOptional.isPresent()) {
            logger.warn("invalid legalId, customer already exists with this legalId - {}", legalId);
            throw new BadRequestException("invalid legalId, customer already exists with this legalId - " + legalId);
        }

        customer.setCreationTime(LocalDateTime.now());

        logger.info("will be saving newly created customer");
        customerRepository.save(customer);

        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO modifyCustomer(CustomerDTO customerDTO) {
        Long id = customerDTO.getId();

        logger.info("will be checking if id - {} is valid", customerDTO.getId());
        if(id == null || id < minimumIdRange) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be checking if customer type is valid");
        if(!CustomerType.isValid(customerDTO.getType().toString())) {
            logger.warn("customer type {} is invalid or undefined", customerDTO.getType().toString());
            throw new BadRequestException("customer type " + customerDTO.getType().toString() + " is invalid or undefined");
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
        if(id < minimumIdRange) {
            logger.warn("invalid id - {}", id);
            throw new BadRequestException("invalid id - " + id);
        }

        logger.info("will be deleting customer of id - {}", id);
        customerRepository.deleteById(id);

        return true;
    }
}
