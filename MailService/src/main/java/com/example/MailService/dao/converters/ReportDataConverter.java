package com.example.MailService.dao.converters;

import com.example.MailService.dao.entity.ReportDataEntity;

import com.example.MailService.models.ReportData;
import com.example.MailService.models.api.Status;
import com.example.MailService.models.api.Type;
import org.springframework.core.convert.converter.Converter;

public class ReportDataConverter implements Converter<ReportDataEntity, ReportData> {


    @Override
    public ReportData convert(ReportDataEntity source) {
        ReportData reportData = new ReportData();
        reportData.setUuid(source.getUuid());
        reportData.setType(Type.valueOf(source.getType()));
        reportData.setToDate(source.getToDate());
        reportData.setFromDate(source.getFromDate());
        reportData.setReportUuid(source.getReportUuid());
        reportData.setStatus(Status.valueOf(source.getStatus()));
        reportData.setMail(source.getMail());
        reportData.setNick(source.getNick());
        reportData.setKey(source.getKey());
        return reportData;
    }

    @Override
    public <U> Converter<ReportDataEntity, U> andThen(Converter<? super ReportData, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
