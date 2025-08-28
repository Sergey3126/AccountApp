package com.example.ClassifierService.dao.converters;


import com.example.ClassifierService.dao.entity.OperationCategoryEntity;

import com.example.ClassifierService.models.OperationCategory;
import org.springframework.core.convert.converter.Converter;

public class OperationCategoryConverterEntity implements Converter<OperationCategory, OperationCategoryEntity> {
    @Override
    public OperationCategoryEntity convert(OperationCategory source) {
        OperationCategoryEntity operationCategoryEntity = new OperationCategoryEntity();
        operationCategoryEntity.setUuid(source.getUuid());
        operationCategoryEntity.setTitle(source.getTitle());
        operationCategoryEntity.setDtCreate(source.getDtCreate());
        operationCategoryEntity.setDtUpdate(source.getDtUpdate());
        return operationCategoryEntity;
    }

    @Override
    public <U> Converter<OperationCategory, U> andThen(Converter<? super OperationCategoryEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
