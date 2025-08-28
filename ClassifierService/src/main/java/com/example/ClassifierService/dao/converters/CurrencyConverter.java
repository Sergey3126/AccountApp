package com.example.ClassifierService.dao.converters;


import com.example.ClassifierService.dao.entity.CurrencyEntity;
import com.example.ClassifierService.models.Currency;
import org.springframework.core.convert.converter.Converter;


public class CurrencyConverter implements Converter<CurrencyEntity, Currency> {
    @Override
    public Currency convert(CurrencyEntity source) {
        Currency currency = new Currency();
        currency.setUuid(source.getUuid());
        currency.setTitle(source.getTitle());
        currency.setDtCreate(source.getDtCreate());
        currency.setDtUpdate(source.getDtUpdate());
        return currency;
    }

    @Override
    public <U> Converter<CurrencyEntity, U> andThen(Converter<? super Currency, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}

