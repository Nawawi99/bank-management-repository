package dev.awn.customermanagementservice.core.customer.dto;

import dev.awn.customermanagementservice.core.customer.constant.CustomerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerDTO {
    private Long id;
    @NotBlank(message = "name cannot be empty")
    private String name;
    @NotBlank(message = "legalId cannot be empty")
    private String legalId;
    @NotBlank(message = "address cannot be empty")
    private String address;
    @NotNull(message = "type cannot be empty")
    private CustomerType type;
}
