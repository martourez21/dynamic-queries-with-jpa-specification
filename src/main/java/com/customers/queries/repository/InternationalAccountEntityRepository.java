package com.customers.queries.repository;

import com.customers.queries.entity.InternationalAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InternationalAccountEntityRepository extends JpaRepository<InternationalAccountEntity, UUID>, JpaSpecificationExecutor<InternationalAccountEntity> {

}
