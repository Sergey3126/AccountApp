package com.example.AccountService.dao.converters;

import com.example.AccountService.dao.entity.AccountEntity;
import com.example.AccountService.models.Account;
import org.springframework.core.convert.converter.Converter;


public class AccountConverterEntity implements Converter<Account, AccountEntity> {
    @Override
    public AccountEntity convert(Account source) {
        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setUuid(source.getUuid());
        accountEntity.setDtCreate(source.getDtCreate());
        accountEntity.setDtUpdate(source.getDtUpdate());
        accountEntity.setTitle(source.getTitle());
        accountEntity.setDescription(source.getDescription());
        accountEntity.setBalance(source.getBalance());
        accountEntity.setType(String.valueOf(source.getType()));
        accountEntity.setCurrency(source.getCurrency());
        accountEntity.setNick(source.getNick());

        return accountEntity;
    }

    @Override
    public <U> Converter<Account, U> andThen(Converter<? super AccountEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
