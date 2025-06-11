package com.customers.queries.mapper;


import com.customers.queries.dto.branch.BranchRequestDto;
import com.customers.queries.dto.branch.BranchResponseDto;
import com.customers.queries.entity.BankBranch;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BankBranchMapper {

    BankBranch toEntity(BranchRequestDto dto);

    BranchResponseDto toDto(BankBranch entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BranchRequestDto dto, @MappingTarget BankBranch entity);
}
