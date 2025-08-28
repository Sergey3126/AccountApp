package com.example.MailService.dao.converters;


import com.example.MailService.dao.entity.ReportDataEntity;
import com.example.MailService.models.ReportData;
import org.springframework.core.convert.converter.Converter;

public class ReportDataConverterEntity implements Converter<ReportData, ReportDataEntity> {


    @Override
    public ReportDataEntity convert(ReportData source) {
        ReportDataEntity reportDataEntity = new ReportDataEntity();
        reportDataEntity.setUuid(source.getUuid());
        reportDataEntity.setType(String.valueOf(source.getType()));
        reportDataEntity.setToDate(source.getToDate());
        reportDataEntity.setFromDate(source.getFromDate());
        reportDataEntity.setReportUuid(source.getReportUuid());
        reportDataEntity.setStatus(String.valueOf(source.getStatus()));
        reportDataEntity.setMail(source.getMail());
        reportDataEntity.setNick(source.getNick());
        reportDataEntity.setKey(source.getKey());
        return reportDataEntity;
    }

    @Override
    public <U> Converter<ReportData, U> andThen(Converter<? super ReportDataEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}