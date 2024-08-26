package dev.awn.customermanagementservice.core.customer.service.impl;

import dev.awn.customermanagementservice.common.exception.BadRequestException;
import dev.awn.customermanagementservice.common.exception.ResourceNotFoundException;
import dev.awn.customermanagementservice.core.customer.constant.CustomerType;
import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.mapper.CustomerMapper;
import dev.awn.customermanagementservice.core.customer.model.Customer;
import dev.awn.customermanagementservice.core.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {


    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private static final long MINIMUM_ID = 1000000L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomer_WhenIdIsValid_ReturnsCustomerDTO() {
        // Given
        long id = 1000001L;
        Customer customer = new Customer();
        CustomerDTO customerDTO = new CustomerDTO();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        // When
        CustomerDTO result = customerService.getCustomer(id);

        // Then
        assertNotNull(result);
        verify(customerRepository).findById(id);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void testGetCustomer_WhenIdIsInvalid_ThrowsBadRequestException() {
        // Given
        long id = MINIMUM_ID - 1;

        // When & Then
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.getCustomer(id));
        assertEquals("invalid id - " + id, thrown.getMessage());
    }

    @Test
    void testGetCustomer_WhenCustomerNotFound_ThrowsResourceNotFoundException() {
        // Given
        long id = 1000001L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(id));
        assertEquals("no customer was found of id - " + id, thrown.getMessage());
    }

    @Test
    void testCreateCustomer_WhenIdIsPresent_ThrowsBadRequestException() {
        // Given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1000001L);

        // When & Then
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.createCustomer(customerDTO));
        assertEquals("newly created customer cannot contain a pre-generated id - " + customerDTO.getId(), thrown.getMessage());
    }


    @Test
    void testCreateCustomer_WhenLegalIdExists_ThrowsBadRequestException() {
        // Given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLegalId("LEGAL123");
        Customer customer = new Customer();
        when(customerMapper.toModel(customerDTO)).thenReturn(customer);
        when(customerRepository.findByLegalId("LEGAL123")).thenReturn(Optional.of(customer));

        // When & Then
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.createCustomer(customerDTO));
        assertEquals("invalid legalId, customer already exists with this legalId - LEGAL123", thrown.getMessage());
    }

    @Test
    void testModifyCustomer_WhenIdIsNull_ThrowsBadRequestException() {
        // Given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(null);

        // When & Then
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.modifyCustomer(customerDTO));
        assertEquals("invalid id - null", thrown.getMessage());
    }

    @Test
    void testModifyCustomer_WhenCustomerNotFound_ThrowsResourceNotFoundException() {
        // Given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1000001L);
        when(customerRepository.findById(1000001L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> customerService.modifyCustomer(customerDTO));
        assertEquals("no customer found of id - 1000001", thrown.getMessage());
    }

    @Test
    void testRemoveCustomer_WhenIdIsInvalid_ThrowsBadRequestException() {
        // Given
        long id = MINIMUM_ID - 1;

        // When & Then
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> customerService.removeCustomer(id));
        assertEquals("invalid id - " + id, thrown.getMessage());
    }

    @Test
    void testRemoveCustomer_WhenIdIsValid_RemovesCustomer() {
        // Given
        long id = 1000001L;

        // When
        boolean result = customerService.removeCustomer(id);

        // Then
        assertTrue(result);
        verify(customerRepository).deleteById(id);
    }
}


