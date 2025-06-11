package com.customers.queries.service.branch;

import com.customers.queries.dto.branch.BranchRequestDto;
import com.customers.queries.dto.branch.BranchResponseDto;
import com.customers.queries.dto.branch.BranchSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchService {
    BranchResponseDto createBranch(BranchRequestDto branchRequestDto);

    Optional<BranchResponseDto> consultDetailsOfBranch(UUID branchId);

    String deleteBranch(UUID branchId);

    BranchResponseDto updateBranch(UUID branchId, BranchRequestDto branchRequestDto);

    Page<BranchResponseDto> consultListOfBranches(Integer page, Integer size, String sort, BranchSearchCriteria filter);
}

