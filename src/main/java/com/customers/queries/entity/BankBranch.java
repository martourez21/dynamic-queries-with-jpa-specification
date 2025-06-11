package com.customers.queries.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "branch_code", unique = true, nullable = false, length = 4)
    private String branchCode;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode = "CM";

    @Column(name = "swift_code", nullable = false, length = 11)
    private String swiftCode;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "XAF";

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean active;

    @PrePersist
    public void generateBranchCode() {
        if (this.branchCode == null || this.branchCode.isBlank()) {
            this.branchCode = String.format("%04d", new SecureRandom().nextInt(10000));
        }
    }
}

