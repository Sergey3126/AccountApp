package com.example.AccountService.dao.converters;

import com.example.AccountService.dao.entity.OperationEntity;
import com.example.AccountService.models.Operation;
import org.springframework.core.convert.converter.Converter;


public class OperationConverterEntity implements Converter<Operation, OperationEntity> {


    @Override
    public OperationEntity convert(Operation source) {


        OperationEntity operationEntity = new OperationEntity();

        operationEntity.setUuid(source.getUuid());
        operationEntity.setDtCreate(source.getDtCreate());
        operationEntity.setDtUpdate(source.getDtUpdate());
        operationEntity.setDate(source.getDate());
        operationEntity.setDescription(source.getDescription());
        operationEntity.setCategory(source.getCategory());
        operationEntity.setAccountUuid(source.getAccountUuid());
        operationEntity.setValue(source.getValue());
        operationEntity.setCurrency(source.getCurrency());
        operationEntity.setNick(source.getNick());
        return operationEntity;
    }

    @Override
    public <U> Converter<Operation, U> andThen(Converter<? super OperationEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
