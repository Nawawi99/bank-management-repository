package dev.awn.customermanagementservice.core.customer.mapper;

import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toModel(CustomerDTO customerDTO) {
        return Customer.builder()
                       .id(customerDTO.getId())
                       .name(customerDTO.getName())
                       .legalId(customerDTO.getLegalId())
                       .address(customerDTO.getAddress())
                       .type(customerDTO.getType())
                       .build();
    }

    public CustomerDTO toDto(Customer customer) {
        return CustomerDTO.builder()
                          .id(customer.getId())
                          .name(customer.getName())
                          .legalId(customer.getLegalId())
                          .address(customer.getAddress())
                          .type(customer.getType())
                          .build();
    }

}
