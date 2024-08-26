package dev.awn.customermanagementservice.core.customer.controller;

import dev.awn.customermanagementservice.core.customer.dto.CustomerDTO;
import dev.awn.customermanagementservice.core.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        logger.info("received request");

        CustomerDTO customerDTO = customerService.getCustomer(id);

        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        logger.info("received request");

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(customerService.createCustomer(customerDTO));
    }

    @PutMapping
    public ResponseEntity<CustomerDTO> modifyCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        logger.info("received request");

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(customerService.modifyCustomer(customerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCustomer(@PathVariable Long id) {
        logger.info("received request");

        customerService.removeCustomer(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
