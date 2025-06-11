package com.customers.queries.controller.local_account;

import com.customers.queries.dto.local_account.AccountRequestDto;
import com.customers.queries.dto.local_account.AccountResponseDto;
import com.customers.queries.dto.local_account.AccountSearchCriteria;
import com.customers.queries.dto.withdraw_money.WithdrawRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/${account.api.version}/bank")
public interface AccountApi {

    @PostMapping("/{branchCode}/accounts")
    ResponseEntity<AccountResponseDto> createAccount(@PathVariable("branchCode") String branchCode,   @RequestBody AccountRequestDto accountRequestDto);

    @GetMapping("/accounts/{accountId}")
    ResponseEntity<AccountResponseDto> consultDetailsOfAccount(@PathVariable UUID accountId);
    @DeleteMapping("/accounts/{accountId}")
    ResponseEntity<String> deleteAccount(@PathVariable UUID accountId);
    @PutMapping("/accounts/{accountId}")
    ResponseEntity<AccountResponseDto> updateAccount(@PathVariable UUID accountId, @RequestBody AccountRequestDto accountRequestDto);
    @GetMapping("/accounts")
    ResponseEntity<Page<AccountResponseDto>> consultListOfAccounts(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            AccountSearchCriteria filter);

    @PostMapping("/accounts/withdraw")
    ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request);
}
