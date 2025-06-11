package com.customers.queries.service.local_account;


import com.customers.queries.config.AccountProperties;
import com.customers.queries.dto.branch.BranchSearchCriteria;
import com.customers.queries.dto.local_account.AccountRequestDto;
import com.customers.queries.dto.local_account.AccountResponseDto;
import com.customers.queries.dto.local_account.AccountSearchCriteria;
import com.customers.queries.entity.AccountEntity;
import com.customers.queries.mapper.AccountMapper;
import com.customers.queries.repository.AccountEntityRepository;
import com.customers.queries.repository.BankBranchRepository;
import com.customers.queries.service.branch.BankBranchSpecification;
import com.sixbank.accountlibrary.utility.AccountNumberGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceHandler implements AccountService{

    private final AccountEntityRepository accountEntityRepository;
    private final BankBranchRepository bankBranchRepository;
    private final AccountProperties properties;
    private final AccountMapper mapper;
    private final KieContainer kieContainer;



    public AccountServiceHandler(AccountEntityRepository accountEntityRepository, BankBranchRepository bankBranchRepository, AccountProperties properties, AccountMapper mapper, KieContainer kieContainer) {
        this.accountEntityRepository = accountEntityRepository;
        this.bankBranchRepository = bankBranchRepository;
        this.properties = properties;
        this.mapper = mapper;
        this.kieContainer = kieContainer;
    }

    @Override
    @Transactional
    public AccountResponseDto createAccount(String branchCode, AccountRequestDto accountRequestDto) {
        var branch = bankBranchRepository.findBankBranchByBranchCode(branchCode)
                .orElseThrow(()-> new RuntimeException("Bank branch not found"));

        var newAccount = mapper.toEntity(accountRequestDto);
        newAccount.setBankBranch(branch);
        String newAccountNumber = AccountNumberGenerator.generateAccountNumber(properties.getPrefix());

        newAccount.setAccountNumber(newAccountNumber);
        var savedAccount = accountEntityRepository.save(newAccount);
        System.out.println("This is the actual account numbers: "+newAccountNumber+" Actual Numeric Number: "+AccountNumberGenerator.extractNumericPart(newAccountNumber,properties.getPrefix())+" Masked number: "+AccountNumberGenerator.maskAccountNumber(newAccountNumber));

        return mapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponseDto consultDetailsOfAccount(UUID accountId) {
        return accountEntityRepository.findById(accountId).map(account -> mapper.toResponse(account)).
                orElseThrow(()-> new EntityNotFoundException("Account not found"));
    }

    @Override
    @Transactional
    public String deleteAccount(UUID accountId) {
        var accountName = accountEntityRepository.findById(accountId).orElseThrow(()-> new EntityNotFoundException("Account not found"));
        accountEntityRepository.deleteById(accountId);

        return "Account: " + accountName + " deleted";
    }

    @Override
    public AccountResponseDto updateAccount(UUID accountId, AccountRequestDto accountRequestDto) {
        var account = accountEntityRepository.findById(accountId).orElseThrow(()-> new EntityNotFoundException("Account not found"));

        var updatedAccount = mapper.updateEntity(account, accountRequestDto);

        accountEntityRepository.save(updatedAccount);

        return mapper.toResponse(updatedAccount);
    }

    @Override
    public Page<AccountResponseDto> consultListOfAccounts(Integer page, Integer size, String sort, AccountSearchCriteria filter) {
        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);
        sort = Optional.ofNullable(sort).orElse("createdAt,desc");
        filter = Optional.ofNullable(filter).orElse(AccountSearchCriteria.createDefault());

        // Validate and parse sort parameter
        String[] sortParams = sort.split(",");
        if (sortParams.length != 2) {
            throw new IllegalArgumentException("Sort parameter must be in format 'field,direction'");
        }

        String sortField = sortParams[0];

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(sortParams[1]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Sort direction must be 'asc' or 'desc'");
        }

        // Create the Sort object
        Sort sorting = Sort.by(sortDirection, sortField);
        Pageable pageable = PageRequest.of(page, size, sorting);

        return accountEntityRepository.findAll(AccountSpecification.withFilter(filter), pageable)
                .map(mapper::toResponse);
    }

    @Override
    public String withdraw(UUID accountId, BigDecimal amount) {
        AccountEntity account = accountEntityRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        WithdrawContext context = new WithdrawContext();
        context.setAccount(account);
        context.setAmount(amount);

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(context);
        kieSession.fireAllRules();
        kieSession.dispose();

        if (context.isRejected()) {
            return "Withdrawal Rejected: " + context.getReason();
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setLastTransactionDate(LocalDateTime.now());
        accountEntityRepository.save(account);

        return "Withdrawal Successful. New Balance: " + account.getBalance();
    }
}
