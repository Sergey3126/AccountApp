package com.example.ReportService.dao.converters;

import com.example.ReportService.dao.entity.CategoryEntity;
import com.example.ReportService.models.Category;
import org.springframework.core.convert.converter.Converter;

public class CategoryConverter implements Converter<CategoryEntity, Category> {


    @Override
    public Category convert(CategoryEntity source) {


        Category category = new Category();

        category.setUuid(source.getUuid());
        category.setCategory(source.getCategory());
        category.setReportUuid(source.getReportUuid());


        return category;
    }

    @Override
    public <U> Converter<CategoryEntity, U> andThen(Converter<? super Category, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
