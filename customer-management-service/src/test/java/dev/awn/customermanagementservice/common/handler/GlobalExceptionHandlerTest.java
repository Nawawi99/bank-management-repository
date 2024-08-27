package dev.awn.customermanagementservice.common.handler;

import dev.awn.customermanagementservice.common.exception.BadRequestException;
import dev.awn.customermanagementservice.common.exception.ResourceNotFoundException;
import dev.awn.customermanagementservice.core.customer.controller.CustomerController;
import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.service.CustomerService;
import dev.awn.customermanagementservice.core.customer.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CustomerController.class)
class GlobalExceptionHandlerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleBadRequestException() throws Exception {
        doThrow(new BadRequestException("Invalid id"))
                .when(customerService).getCustomer(1L);

        mockMvc.perform(get("/customers/1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void testHandleResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Invalid id"))
                .when(customerService).getCustomer(1_000_000L);

        mockMvc.perform(get("/customers/1000000"))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void testHandleMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/customers")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{ \"name\": \"ahmad\" }")) // Replace with actual invalid request body
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void testHandleHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(post("/customers")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("")) // Replace with invalid JSON body
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void testHandleDataAccessException() throws Exception {
        doThrow(new DataIntegrityViolationException("DB error"))
                .when(customerService).createCustomer(any(CustomerDTO.class));

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "ahmad",
                                    "legalId": "RJq5Ef",
                                    "address": "Amman",
                                    "type": "RETAIL"
                                }
                                """))
               .andExpect(status().isServiceUnavailable())
               .andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()));
    }
}
