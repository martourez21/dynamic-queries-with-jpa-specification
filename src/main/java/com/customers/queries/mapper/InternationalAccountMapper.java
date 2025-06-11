package com.customers.queries.mapper;

import com.customers.queries.dto.local_account.AccountRequestDto;
import com.customers.queries.dto.local_account.AccountResponseDto;
import com.customers.queries.entity.AccountEntity;
import com.customers.queries.entity.BankBranch;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InternationalAccountMapper {

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastTransactionDate", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now())")
    AccountEntity toEntity(AccountRequestDto request);

    @InheritInverseConfiguration
    @Mapping(target = "branchCode", source = "bankBranch.branchCode")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    AccountResponseDto toResponse(AccountEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lastTransactionDate", expression = "java(java.time.LocalDateTime.now())")
    AccountEntity updateEntity(@MappingTarget AccountEntity entity, AccountRequestDto request);

    // Helper method to convert branchCode to BankBranch
    default BankBranch map(String branchCode) {
        if (branchCode == null) return null;
        var entity = new BankBranch();
        entity.setBranchCode(branchCode);
        return entity;
    }
}
