package com.example.ReportService.dao.converters;


import com.example.ReportService.dao.entity.ResultEntity;
import com.example.ReportService.models.Result;
import org.springframework.core.convert.converter.Converter;

public class ResultConverterEntity implements Converter<Result, ResultEntity> {


    @Override
    public ResultEntity convert(Result source) {


        ResultEntity resultEntity = new ResultEntity();

        resultEntity.setUuid(source.getUuid());
        resultEntity.setResult(source.getResult());
        resultEntity.setReportUuid(source.getReportUuid());

        return resultEntity;
    }

    @Override
    public <U> Converter<Result, U> andThen(Converter<? super ResultEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}