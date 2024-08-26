package dev.awn.customermanagementservice.core.customer.service.impl;

import dev.awn.customermanagementservice.common.exception.BadRequestException;
import dev.awn.customermanagementservice.common.exception.ResourceNotFoundException;
import dev.awn.customermanagementservice.core.customer.constant.CustomerType;
import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.mapper.CustomerMapper;
import dev.awn.customermanagementservice.core.customer.model.Customer;
import dev.awn.customermanagementservice.core.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private static final long MINIMUM_ID = 1000000L;

    @Test
    void testGetCustomer_WhenIdIsValid_ReturnsCustomerDTO() {
        // arrange
        long id = 1000001L;
        Customer customer = new Customer();
        customer.setId(1000001L);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1000001L);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        // act
        CustomerDTO result = customerService.getCustomer(id);

        // asset
        assertNotNull(result);
        verify(customerRepository).findById(id);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void testGetCustomer_WhenIdIsInvalid_ThrowsBadRequestException() {
        // arrange
        long id = MINIMUM_ID - 1;

        // act
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.getCustomer(id));

        // assert
        assertEquals("invalid id - " + id, thrown.getMessage());
    }

    @Test
    void testGetCustomer_WhenCustomerNotFound_ThrowsResourceNotFoundException() {
        // arrange
        long id = 1000001L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // act
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(id));

        // assert
        assertEquals("no customer was found of id - " + id, thrown.getMessage());
    }

    @Test
    void testCreateCustomer_WhenCustomerDTOIsValid_ReturnsCreatedCustomerDTO() {
        // arrange
        CustomerDTO customerDTO = CustomerDTO.builder()
                                             .name("John Doe")
                                             .legalId("LEGAL123")
                                             .address("123 Main St")
                                             .type(CustomerType.RETAIL)
                                             .build();
        Customer customer = Customer.builder()
                                    .name("John Doe")
                                    .legalId("LEGAL123")
                                    .address("123 Main St")
                                    .type(CustomerType.RETAIL)
                                    .build();

        when(customerMapper.toModel(customerDTO)).thenReturn(customer);
        when(customerRepository.findByLegalId(customerDTO.getLegalId())).thenReturn(Optional.empty());
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        // act
        CustomerDTO result = customerService.createCustomer(customerDTO);

        // assert
        assertNotNull(result);
        assertEquals(customerDTO.getLegalId(), result.getLegalId());
        verify(customerRepository).save(customer);
    }


    @Test
    void testCreateCustomer_WhenLegalIdExists_ThrowsBadRequestException() {
        // arrange
        String legalId = "QWE";
        CustomerDTO customerDTO = CustomerDTO.builder()
                                             .legalId(legalId)
                                             .build();
        Customer customer = Customer.builder()
                                    .legalId(legalId)
                                    .build();
        Customer existingCustomer = Customer.builder()
                                            .legalId(legalId)
                                            .build();
        when(customerMapper.toModel(customerDTO)).thenReturn(customer);
        when(customerRepository.findByLegalId(legalId)).thenReturn(Optional.of(existingCustomer));

        // act
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.createCustomer(customerDTO));

        // assert
        assertEquals("invalid legalId, customer already exists with this legalId - QWE", thrown.getMessage());
    }

    @Test
    void testModifyCustomer_WhenIdIsNull_ThrowsBadRequestException() {
        // arrange
        CustomerDTO customerDTO = new CustomerDTO();

        // act
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.modifyCustomer(customerDTO));

        // assert
        assertEquals("invalid id - null", thrown.getMessage());
    }

    @Test
    void testModifyCustomer_WhenCustomerNotFound_ThrowsResourceNotFoundException() {
        // arrange
        long id = 1000000L;
        CustomerDTO customerDTO =  CustomerDTO.builder()
                                              .id(id)
                                              .build();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // act
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> customerService.modifyCustomer(customerDTO));

        // assert
        assertEquals("no customer found of id - 1000000", thrown.getMessage());
    }

    @Test
    void testRemoveCustomer_WhenIdIsInvalid_ThrowsBadRequestException() {
        // arrange
        long id = MINIMUM_ID - 1;

        // act
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.removeCustomer(id));

        // assert
        assertEquals("invalid id - " + id, thrown.getMessage());
    }

    @Test
    void testRemoveCustomer_WhenIdIsValid_RemovesCustomer() {
        // arrange
        long id = 1000000L;

        // act
        boolean result = customerService.removeCustomer(id);

        // assert
        assertTrue(result);
        verify(customerRepository).deleteById(id);
    }
}


