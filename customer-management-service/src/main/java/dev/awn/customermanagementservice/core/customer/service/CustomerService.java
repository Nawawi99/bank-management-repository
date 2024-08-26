package dev.awn.customermanagementservice.core.customer.service;

import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    CustomerDTO getCustomer(long id);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO modifyCustomer(CustomerDTO customerDTO);

    boolean removeCustomer(long id);
}
