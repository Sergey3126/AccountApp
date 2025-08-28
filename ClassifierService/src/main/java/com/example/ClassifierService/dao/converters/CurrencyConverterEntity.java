package com.example.ClassifierService.dao.converters;

import com.example.ClassifierService.dao.entity.CurrencyEntity;
import com.example.ClassifierService.models.Currency;
import org.springframework.core.convert.converter.Converter;

public class CurrencyConverterEntity implements Converter<Currency, CurrencyEntity> {
    @Override
    public CurrencyEntity convert(Currency source) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setUuid(source.getUuid());
        currencyEntity.setTitle(source.getTitle());
        currencyEntity.setDtCreate(source.getDtCreate());
        currencyEntity.setDtUpdate(source.getDtUpdate());
        return currencyEntity;
    }

    @Override
    public <U> Converter<Currency, U> andThen(Converter<? super CurrencyEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
