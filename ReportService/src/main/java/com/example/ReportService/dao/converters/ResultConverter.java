package com.example.ReportService.dao.converters;

import com.example.ReportService.dao.entity.ResultEntity;
import com.example.ReportService.models.Result;
import org.springframework.core.convert.converter.Converter;

public class ResultConverter implements Converter<ResultEntity, Result> {


    @Override
    public Result convert(ResultEntity source) {


        Result result = new Result();
        result.setUuid(source.getUuid());
        result.setResult(source.getResult());
        result.setReportUuid(source.getReportUuid());


        return result;
    }

    @Override
    public <U> Converter<ResultEntity, U> andThen(Converter<? super Result, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
