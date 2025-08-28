package com.example.AccountService.dao.converters;

import com.example.AccountService.dao.entity.AccountEntity;
import com.example.AccountService.models.Account;
import com.example.AccountService.models.api.TypeOfAccount;
import org.springframework.core.convert.converter.Converter;


public class AccountConverter implements Converter<AccountEntity, Account> {


    @Override
    public Account convert(AccountEntity source) {
        Account account = new Account();
        account.setUuid(source.getUuid());
        account.setDtCreate(source.getDtCreate());
        account.setDtUpdate(source.getDtUpdate());
        account.setTitle(source.getTitle());
        account.setDescription(source.getDescription());
        account.setBalance(source.getBalance());
        account.setType(TypeOfAccount.valueOf(source.getType()));
        account.setCurrency(source.getCurrency());
        account.setNick(source.getNick());

        return account;
    }

    @Override
    public <U> Converter<AccountEntity, U> andThen(Converter<? super Account, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
