package com.customers.queries.service.local_account;

import com.customers.queries.dto.local_account.AccountRequestDto;
import com.customers.queries.dto.local_account.AccountResponseDto;
import com.customers.queries.dto.local_account.AccountSearchCriteria;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountResponseDto createAccount(String branchCode, AccountRequestDto accountRequestDto);

    AccountResponseDto consultDetailsOfAccount(UUID accountId);

    String deleteAccount(UUID accountId);

    AccountResponseDto updateAccount(UUID accountId, AccountRequestDto accountRequestDto);

    Page<AccountResponseDto> consultListOfAccounts(Integer page, Integer size, String sort, AccountSearchCriteria filter);

    String withdraw(UUID accountId, BigDecimal amount);
}
