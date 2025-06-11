package com.customers.queries.repository;

import com.customers.queries.entity.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch, UUID>, JpaSpecificationExecutor<BankBranch> {

    Optional<BankBranch> findBankBranchByBranchCode(String branchCode);
}
