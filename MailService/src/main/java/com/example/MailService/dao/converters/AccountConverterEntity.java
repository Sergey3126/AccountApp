package com.example.MailService.dao.converters;

import com.example.MailService.dao.entity.AccountsEntity;
import com.example.MailService.models.Accounts;
import org.springframework.core.convert.converter.Converter;

public class AccountConverterEntity implements Converter<Accounts, AccountsEntity> {


    @Override
    public AccountsEntity convert(Accounts source) {
        AccountsEntity accountEntity = new AccountsEntity();
        accountEntity.setUuid(source.getUuid());
        accountEntity.setAccount(source.getAccount());
        accountEntity.setReportUuid(source.getReportUuid());
        return accountEntity;
    }

    @Override
    public <U> Converter<Accounts, U> andThen(Converter<? super AccountsEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}