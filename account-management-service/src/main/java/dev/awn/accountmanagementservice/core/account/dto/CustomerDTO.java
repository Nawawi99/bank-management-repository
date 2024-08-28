package dev.awn.accountmanagementservice.core.account.dto;

import dev.awn.accountmanagementservice.core.account.constant.CustomerType;
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

