package com.customers.queries.dto.branch;

public record BranchRequestDto(
        String branchName,
        String countryCode,
        String swiftCode,
        String currency,
        Boolean active
) {}