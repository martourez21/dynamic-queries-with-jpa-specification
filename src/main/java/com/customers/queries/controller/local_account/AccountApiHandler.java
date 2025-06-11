package com.customers.queries.controller.local_account;

import com.customers.queries.dto.local_account.AccountRequestDto;
import com.customers.queries.dto.local_account.AccountResponseDto;
import com.customers.queries.dto.local_account.AccountSearchCriteria;
import com.customers.queries.dto.withdraw_money.WithdrawRequest;
import com.customers.queries.service.local_account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountApiHandler implements AccountApi{

    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountResponseDto> createAccount(String branchCode, AccountRequestDto accountRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(branchCode, accountRequestDto));
    }

    @Override
    public ResponseEntity<AccountResponseDto> consultDetailsOfAccount(UUID accountId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.consultDetailsOfAccount(accountId));
    }

    @Override
    public ResponseEntity<String> deleteAccount(UUID accountId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.deleteAccount(accountId));
    }

    @Override
    public ResponseEntity<AccountResponseDto> updateAccount(UUID accountId, AccountRequestDto accountRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.updateAccount(accountId, accountRequestDto));
    }

    @Override
    public ResponseEntity<Page<AccountResponseDto>> consultListOfAccounts(Integer page, Integer size, String sort, AccountSearchCriteria filter) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.consultListOfAccounts(page, size, sort, filter));
    }

    @Override
    public ResponseEntity<String> withdraw(WithdrawRequest request) {
        String result = accountService.withdraw(request.getAccountId(), request.getAmount());
        return ResponseEntity.ok(result);
    }
}
