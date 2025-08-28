package com.example.ReportService.dao.converters;

import com.example.ReportService.dao.entity.ReportEntity;
import com.example.ReportService.models.Report;
import org.springframework.core.convert.converter.Converter;

public class ReportConverterEntity implements Converter<Report, ReportEntity> {


    @Override
    public ReportEntity convert(Report source) {
        ReportEntity reportEntity = new ReportEntity();

        reportEntity.setUuid(source.getUuid());
        reportEntity.setDtCreate(source.getDtCreate());
        reportEntity.setDtUpdate(source.getDtUpdate());
        reportEntity.setDescription(source.getDescription());
        reportEntity.setStatus(String.valueOf(source.getStatus()));
        reportEntity.setType(String.valueOf(source.getType()));
        reportEntity.setToDate(source.getToDate());
        reportEntity.setFromDate(source.getFromDate());
reportEntity.setKey(source.getKey());
        reportEntity.setNick(source.getNick());
        return reportEntity;
    }

    @Override
    public <U> Converter<Report, U> andThen(Converter<? super ReportEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}