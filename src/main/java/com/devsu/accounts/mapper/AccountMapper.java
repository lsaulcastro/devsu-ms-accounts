package com.devsu.accounts.mapper;

import com.devsu.accounts.domain.model.Account;
import com.devsu.accounts.dto.AccountResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponseList(List<Account> accounts);
}