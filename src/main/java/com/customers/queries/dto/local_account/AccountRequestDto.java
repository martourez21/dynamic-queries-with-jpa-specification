package com.customers.queries.dto.local_account;


import com.sixbank.accountlibrary.enums.AccountStatus;
import com.sixbank.accountlibrary.enums.AccountType;

import java.math.BigDecimal;

public record AccountRequestDto(
        String accountHolderName,
        AccountType accountType,
        BigDecimal balance,
        AccountStatus status,
        String branchCode,
        BigDecimal interestRate
) {}

