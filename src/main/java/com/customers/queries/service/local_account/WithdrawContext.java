package com.customers.queries.service.local_account;

import com.customers.queries.entity.AccountEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawContext {
    private AccountEntity account;
    private BigDecimal amount;
    private boolean rejected = false;
    private String reason;
}
