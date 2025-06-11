package com.customers.queries.dto.withdraw_money;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WithdrawRequest {
    private UUID accountId;
    private BigDecimal amount;
}
