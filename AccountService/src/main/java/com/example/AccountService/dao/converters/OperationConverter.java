package com.example.AccountService.dao.converters;


import com.example.AccountService.dao.entity.OperationEntity;

import com.example.AccountService.models.Operation;
import org.springframework.core.convert.converter.Converter;


public class OperationConverter implements Converter<OperationEntity, Operation> {


    @Override
    public Operation convert(OperationEntity source) {
        Operation operation = new Operation();

        operation.setNick(source.getNick());
        operation.setUuid(source.getUuid());
        operation.setDtCreate(source.getDtCreate());
        operation.setDtUpdate(source.getDtUpdate());
        operation.setDate(source.getDate());
        operation.setDescription(source.getDescription());
        operation.setValue(source.getValue());
        operation.setCurrency(source.getCurrency());
        operation.setCategory(source.getCategory());
        operation.setAccountUuid(source.getAccountUuid());
        return operation;
    }

    @Override
    public <U> Converter<OperationEntity, U> andThen(Converter<? super Operation, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
