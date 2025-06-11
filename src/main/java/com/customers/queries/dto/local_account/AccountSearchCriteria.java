package com.customers.queries.dto.local_account;

import com.customers.queries.dto.branch.BranchSearchCriteria;
import com.sixbank.accountlibrary.enums.AccountStatus;
import com.sixbank.accountlibrary.enums.AccountType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class AccountSearchCriteria {
    private String accountHolderName;
    private AccountType accountType;
    private List<AccountType> accountTypes;
    private AccountStatus status;
    private BigDecimal minBalance;
    private BigDecimal maxBalance;
    private String branchCode;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private Integer recentActivityDays;
    private BigDecimal minInterestRate;
    private BigDecimal maxInterestRate;
    private Integer dormantMonths;

    public static AccountSearchCriteria createDefault() {
        return new AccountSearchCriteria();
    }

}
