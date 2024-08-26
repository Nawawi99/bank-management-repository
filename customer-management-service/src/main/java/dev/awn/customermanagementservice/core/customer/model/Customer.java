package dev.awn.customermanagementservice.core.customer.model;

import dev.awn.customermanagementservice.core.customer.constant.CustomerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LEGAL_ID", unique = true)
    private String legalId;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private CustomerType type;

    // Updatable false cuz we never map it with a value
    @Column(name = "CREATION_TIME", updatable = false)
    private LocalDateTime creationTime;

    @Column(name = "MODIFICATION_TIME")
    private LocalDateTime modificationTime;
}
