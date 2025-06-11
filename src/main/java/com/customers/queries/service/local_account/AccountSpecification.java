package com.customers.queries.service.local_account;

import com.customers.queries.dto.local_account.AccountSearchCriteria;
import com.customers.queries.entity.AccountEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountSpecification {

    public static Specification<AccountEntity> withFilter(AccountSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getAccountHolderName() != null) {
                predicates.add(cb.like(cb.lower(root.get("accountHolderName")),
                        "%" + criteria.getAccountHolderName().toLowerCase() + "%"));
            }

            if (criteria.getAccountType() != null) {
                predicates.add(cb.equal(root.get("accountType"), criteria.getAccountType()));
            }

            if (criteria.getAccountTypes() != null && !criteria.getAccountTypes().isEmpty()) {
                predicates.add(root.get("accountType").in(criteria.getAccountTypes()));
            }

            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            }

            if (criteria.getMinBalance() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("balance"), criteria.getMinBalance()));
            }

            if (criteria.getMaxBalance() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("balance"), criteria.getMaxBalance()));
            }

            if (criteria.getBranchCode() != null) {
                predicates.add(cb.equal(root.get("branchCode"), criteria.getBranchCode()));
            }

            if (criteria.getCreatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.getCreatedAfter()));
            }

            if (criteria.getCreatedBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.getCreatedBefore()));
            }

            if (criteria.getMinInterestRate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("interestRate"), criteria.getMinInterestRate()));
            }

            if (criteria.getMaxInterestRate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("interestRate"), criteria.getMaxInterestRate()));
            }

            // Combine all predicates with AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
