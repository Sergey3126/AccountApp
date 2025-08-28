package com.example.ClassifierService.dao.converters;


import com.example.ClassifierService.dao.entity.OperationCategoryEntity;

import com.example.ClassifierService.models.OperationCategory;
import org.springframework.core.convert.converter.Converter;


public class OperationCategoryConverter implements Converter<OperationCategoryEntity, OperationCategory> {
    @Override
    public OperationCategory convert(OperationCategoryEntity source) {
        OperationCategory operationCategory = new OperationCategory();
        operationCategory.setUuid(source.getUuid());
        operationCategory.setTitle(source.getTitle());
        operationCategory.setDtCreate(source.getDtCreate());
        operationCategory.setDtUpdate(source.getDtUpdate());
        return operationCategory;
    }

    @Override
    public <U> Converter<OperationCategoryEntity, U> andThen(Converter<? super OperationCategory, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}

