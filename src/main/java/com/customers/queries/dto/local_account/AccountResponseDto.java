package com.customers.queries.dto.local_account;

import com.sixbank.accountlibrary.enums.AccountStatus;
import com.sixbank.accountlibrary.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDto(
        UUID accountId,
        String accountNumber,
        String accountHolderName,
        AccountType accountType,
        BigDecimal balance,
        AccountStatus status,
        String branchCode,
        LocalDateTime createdDate,
        LocalDateTime lastTransactionDate,
        BigDecimal interestRate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

