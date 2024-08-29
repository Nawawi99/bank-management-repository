package dev.awn.customermanagementservice.core.customer.controller;

import dev.awn.customermanagementservice.core.customer.constant.CustomerType;
import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private static final long CUSTOMER_ID = 1_000_000L;

    @Test
    void testGetCustomer_WhenCustomerExists_ReturnsCustomerDTO() throws Exception {
        // arrange
        CustomerDTO customerDTO = CustomerDTO.builder()
                                             .id(CUSTOMER_ID)
                                             .name("John Doe")
                                             .legalId("LEGAL123")
                                             .address("123 Main St")
                                             .type(CustomerType.RETAIL)
                                             .build();
        when(customerService.getCustomer(CUSTOMER_ID)).thenReturn(customerDTO);

        // act
        mockMvc.perform(get("/customers/{id}", CUSTOMER_ID))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CUSTOMER_ID))
               .andExpect(jsonPath("$.name").value("John Doe"))
               .andExpect(jsonPath("$.legalId").value("LEGAL123"))
               .andExpect(jsonPath("$.address").value("123 Main St"))
               .andExpect(jsonPath("$.type").value(CustomerType.RETAIL.toString()));

        // assert
        verify(customerService).getCustomer(CUSTOMER_ID);
    }

    @Test
    void testCreateCustomer_WhenCustomerIsValid_ReturnsCreatedCustomerDTO() throws Exception {
        // arrange
        CustomerDTO customerDTO = CustomerDTO.builder()
                                             .name("Jane Doe")
                                             .legalId("LEGAL456")
                                             .address("456 Elm St")
                                             .type(CustomerType.CORPORATE)
                                             .build();

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        // act
        mockMvc.perform(post("/customers")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"name\":\"Jane Doe\",\"legalId\":\"LEGAL456\",\"address\":\"456 Elm St\",\"type\":\"CORPORATE\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("Jane Doe"))
               .andExpect(jsonPath("$.legalId").value("LEGAL456"))
               .andExpect(jsonPath("$.address").value("456 Elm St"))
               .andExpect(jsonPath("$.type").value(CustomerType.CORPORATE.toString()));

        // assert
        verify(customerService).createCustomer(any(CustomerDTO.class));
    }

    @Test
    void testModifyCustomer_WhenCustomerIsValid_ReturnsModifiedCustomerDTO() throws Exception {
        // arrange
        CustomerDTO modifiedCustomerDTO = CustomerDTO.builder()
                                                     .name("Jane Smith")
                                                     .legalId("LEGAL789")
                                                     .address("789 Pine St")
                                                     .type(CustomerType.RETAIL)
                                                     .build();

        when(customerService.modifyCustomer(any(CustomerDTO.class))).thenReturn(modifiedCustomerDTO);

        // act
        mockMvc.perform(put("/customers")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"name\":\"Jane Smith\",\"legalId\":\"LEGAL789\",\"address\":\"789 Pine St\",\"type\":\"RETAIL\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("Jane Smith"))
               .andExpect(jsonPath("$.legalId").value("LEGAL789"))
               .andExpect(jsonPath("$.address").value("789 Pine St"))
               .andExpect(jsonPath("$.type").value(CustomerType.RETAIL.toString()));

        // assert
        verify(customerService).modifyCustomer(any(CustomerDTO.class));
    }

    @Test
    void testRemoveCustomer_WhenCustomerExists_ReturnsOk() throws Exception {
        // act
        mockMvc.perform(delete("/customers/{id}", CUSTOMER_ID))
               .andExpect(status().isOk());

        // assert
        verify(customerService).removeCustomer(CUSTOMER_ID);
    }
}
