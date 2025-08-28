package com.example.ReportService.dao.converters;

import com.example.ReportService.dao.entity.CategoryEntity;
import com.example.ReportService.models.Category;
import org.springframework.core.convert.converter.Converter;

public class CategoryConverterEntity implements Converter<Category, CategoryEntity> {


    @Override
    public CategoryEntity convert(Category source) {


        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setUuid(source.getUuid());
        categoryEntity.setCategory(source.getCategory());
        categoryEntity.setReportUuid(source.getReportUuid());


        return categoryEntity;
    }

    @Override
    public <U> Converter<Category, U> andThen(Converter<? super CategoryEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}