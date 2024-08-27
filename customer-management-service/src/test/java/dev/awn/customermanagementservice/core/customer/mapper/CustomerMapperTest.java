package dev.awn.customermanagementservice.core.customer.mapper;

import dev.awn.customermanagementservice.core.customer.constant.CustomerType;
import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.mapper.CustomerMapper;
import dev.awn.customermanagementservice.core.customer.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CustomerMapperTest {

    private CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void testToModel_WhenCustomerDTOIsValid_ReturnsCustomer() {
        // arrange
        CustomerDTO customerDTO = CustomerDTO.builder()
                                             .id(1000001L)
                                             .name("John Doe")
                                             .legalId("LEGAL123")
                                             .address("123 Main St")
                                             .type(CustomerType.RETAIL)
                                             .build();

        // act
        Customer customer = customerMapper.toModel(customerDTO);

        // assert
        assertNotNull(customer);
        assertEquals(customerDTO.getId(), customer.getId());
        assertEquals(customerDTO.getName(), customer.getName());
        assertEquals(customerDTO.getLegalId(), customer.getLegalId());
        assertEquals(customerDTO.getAddress(), customer.getAddress());
        assertEquals(customerDTO.getType(), customer.getType());
    }

    @Test
    void testToDto_WhenCustomerIsValid_ReturnsCustomerDTO() {
        // arrange
        Customer customer = Customer.builder()
                                    .id(1000001L)
                                    .name("Jane Doe")
                                    .legalId("LEGAL456")
                                    .address("456 Elm St")
                                    .type(CustomerType.CORPORATE)
                                    .build();

        // act
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // assert
        assertNotNull(customerDTO);
        assertEquals(customer.getId(), customerDTO.getId());
        assertEquals(customer.getName(), customerDTO.getName());
        assertEquals(customer.getLegalId(), customerDTO.getLegalId());
        assertEquals(customer.getAddress(), customerDTO.getAddress());
        assertEquals(customer.getType(), customerDTO.getType());
    }
}