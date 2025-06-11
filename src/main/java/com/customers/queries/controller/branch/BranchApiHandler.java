package com.customers.queries.controller.branch;

import com.customers.queries.dto.branch.BranchRequestDto;
import com.customers.queries.dto.branch.BranchResponseDto;
import com.customers.queries.dto.branch.BranchSearchCriteria;
import com.customers.queries.service.branch.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BranchApiHandler implements BranchApi{
    
    private final BranchService branchService;

    @Override
    public ResponseEntity<BranchResponseDto> createBranch(BranchRequestDto branchRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(branchService.createBranch(branchRequestDto));
    }

    @Override
    public ResponseEntity<BranchResponseDto> consultDetailsOfBranch(UUID branchId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(branchService.consultDetailsOfBranch(branchId).get());
    }

    @Override
    public ResponseEntity<String> deleteBranch(UUID branchId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(branchService.deleteBranch(branchId));
    }

    @Override
    public ResponseEntity<BranchResponseDto> updateBranch(UUID branchId, BranchRequestDto branchRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(branchService.updateBranch(branchId, branchRequestDto));
    }

    @Override
    public ResponseEntity<Page<BranchResponseDto>> consultListOfBranches(Integer page, Integer size, String sort, BranchSearchCriteria filter) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(branchService.consultListOfBranches(page, size, sort, filter));
    }
}
