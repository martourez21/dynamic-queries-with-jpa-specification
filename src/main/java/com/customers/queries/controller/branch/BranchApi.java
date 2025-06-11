package com.customers.queries.controller.branch;

import com.customers.queries.dto.branch.BranchRequestDto;
import com.customers.queries.dto.branch.BranchResponseDto;
import com.customers.queries.dto.branch.BranchSearchCriteria;
import com.customers.queries.dto.local_account.AccountSearchCriteria;
import com.customers.queries.entity.BankBranch;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/${account.api.version}/branches")
public interface BranchApi {
    @PostMapping
    ResponseEntity<BranchResponseDto> createBranch(@RequestBody BranchRequestDto branchRequestDto);

    @GetMapping("/{branchId}")
    ResponseEntity<BranchResponseDto> consultDetailsOfBranch(@PathVariable UUID branchId);
    @DeleteMapping("/{branchId}")
    ResponseEntity<String> deleteBranch(@PathVariable UUID branchId);
    @PutMapping("/{branchId}")
    ResponseEntity<BranchResponseDto> updateBranch(@PathVariable UUID branchId, @RequestBody BranchRequestDto branchRequestDto);
    @GetMapping
    ResponseEntity<Page<BranchResponseDto>> consultListOfBranches(
             @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
             @RequestParam(value = "sort", required = false) String sort,
            BranchSearchCriteria filter);

}
