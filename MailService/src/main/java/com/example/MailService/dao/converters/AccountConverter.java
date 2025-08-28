package com.example.MailService.dao.converters;

import com.example.MailService.dao.entity.AccountsEntity;
import com.example.MailService.models.Accounts;
import org.springframework.core.convert.converter.Converter;

public class AccountConverter implements Converter<AccountsEntity, Accounts> {


    @Override
    public Accounts convert(AccountsEntity source) {
        Accounts account = new Accounts();
        account.setUuid(source.getUuid());
        account.setAccount(source.getAccount());
        account.setReportUuid(source.getReportUuid());
        return account;
    }

    @Override
    public <U> Converter<AccountsEntity, U> andThen(Converter<? super Accounts, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
