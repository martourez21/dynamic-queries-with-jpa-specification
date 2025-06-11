package com.customers.queries.service.branch;

import com.customers.queries.dto.branch.BranchRequestDto;
import com.customers.queries.dto.branch.BranchResponseDto;
import com.customers.queries.dto.branch.BranchSearchCriteria;
import com.customers.queries.entity.BankBranch;
import com.customers.queries.mapper.BankBranchMapper;
import com.customers.queries.repository.BankBranchRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class BranchServiceHandler implements BranchService{

    private final BankBranchMapper bankBranchMapper;

    private final BankBranchRepository repository;

    public BranchServiceHandler(BankBranchMapper bankBranchMapper, BankBranchRepository repository) {
        this.bankBranchMapper = bankBranchMapper;
        this.repository = repository;
    }


    @Override
    public BranchResponseDto createBranch(BranchRequestDto branchRequestDto) {
        var newBranch = bankBranchMapper.toEntity(branchRequestDto);
        newBranch = repository.save(newBranch);

        return bankBranchMapper.toDto(newBranch);
    }

    @Override
    public Optional<BranchResponseDto> consultDetailsOfBranch(UUID branchId) {
        return Optional.ofNullable(Optional.ofNullable(bankBranchMapper.toDto(repository.findById(branchId).get()))
                .orElseThrow(() -> new EntityNotFoundException("Branch not found!")));
    }

    @Override
    @Transactional
    public String deleteBranch(UUID branchId) {
        BankBranch branch = repository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch with ID " + branchId + " not found"));

        String branchName = branch.getBranchName();
        repository.delete(branch);

        return "Branch '" + branchName + "' has been successfully deleted.";
    }

    @Override
    @Transactional
    public BranchResponseDto updateBranch(UUID branchId, BranchRequestDto branchRequestDto) {
        if (branchRequestDto == null) {
            throw new IllegalArgumentException("Branch request cannot be null");
        }

        BankBranch existingBranch = repository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch with ID " + branchId + " not found"));

        Optional.ofNullable(branchRequestDto.branchName()).ifPresent(existingBranch::setBranchName);
        Optional.ofNullable(branchRequestDto.swiftCode()).ifPresent(existingBranch::setSwiftCode);
        Optional.ofNullable(branchRequestDto.countryCode()).ifPresent(existingBranch::setCountryCode);
        Optional.ofNullable(branchRequestDto.currency()).ifPresent(existingBranch::setCurrency);
        Optional.ofNullable(branchRequestDto.active()).ifPresent(existingBranch::setActive);

        BankBranch updatedBranch = repository.save(existingBranch);
        return bankBranchMapper.toDto(updatedBranch);
    }


    @Override
    public Page<BranchResponseDto> consultListOfBranches(Integer page, Integer size, String sort, BranchSearchCriteria filter) {
        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);
        sort = Optional.ofNullable(sort).orElse("createdAt,desc");
        filter = Optional.ofNullable(filter).orElse(BranchSearchCriteria.createDefault());

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

        return repository.findAll(BankBranchSpecification.withFilter(filter), pageable)
                .map(bankBranchMapper::toDto);
    }

}
