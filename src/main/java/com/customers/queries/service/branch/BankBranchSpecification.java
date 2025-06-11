package com.customers.queries.service.branch;

import com.customers.queries.dto.branch.BranchSearchCriteria;
import com.customers.queries.entity.BankBranch;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BankBranchSpecification {

    public static Specification<BankBranch> withFilter(BranchSearchCriteria criteria){
        return(root, query, criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();

            //filter by active or inactive status
            if(criteria.getActive() != null){
                predicates.add(criteriaBuilder.equal(root.get("active"), criteria.getActive()));
            }

            if (criteria.getCreatedBefore() != null) {
                Expression<LocalDate> updatedAtDate = criteriaBuilder.function("DATE", LocalDate.class, root.get("createdAt"));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(updatedAtDate, criteria.getCreatedBefore()));
            }

            // Filter by dateTo (createdAt <= dateTo)
            if (criteria.getCreatedAfter() != null) {
                Expression<LocalDate> updatedAtDate = criteriaBuilder.function("DATE", LocalDate.class, root.get("updatedAt"));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(updatedAtDate, criteria.getCreatedAfter()));
            }


            if (criteria.getBranchCode() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("branchCode"), criteria.getBranchCode()));
            }

            if (criteria.getBranchName() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("brandName"), criteria.getBranchName()));
            }

            if (criteria.getCountryCode() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("countryCode"), criteria.getCountryCode()));
            }
            if (criteria.getSwiftCode() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("swiftCode"), criteria.getSwiftCode()));
            }

            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
