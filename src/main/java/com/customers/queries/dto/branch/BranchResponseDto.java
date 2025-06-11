package com.customers.queries.dto.branch;

import java.time.LocalDateTime;
import java.util.UUID;

public record BranchResponseDto(
        UUID id,
        String branchCode,
        String branchName,
        String countryCode,
        String swiftCode,
        String currency,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
