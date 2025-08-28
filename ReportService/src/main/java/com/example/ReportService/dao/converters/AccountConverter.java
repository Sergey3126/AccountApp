package com.example.ReportService.dao.converters;

import com.example.ReportService.dao.entity.AccountsEntity;
import com.example.ReportService.models.Accounts;
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
