package com.example.ReportService.dao.converters;

import com.example.ReportService.dao.entity.ReportEntity;
import com.example.ReportService.models.Report;

import com.example.ReportService.models.api.Status;
import com.example.ReportService.models.api.Type;
import org.springframework.core.convert.converter.Converter;

public class ReportConverter implements Converter<ReportEntity, Report> {


    @Override
    public Report convert(ReportEntity source) {


        Report report = new Report();

        report.setUuid(source.getUuid());
        report.setDtCreate(source.getDtCreate());
        report.setDtUpdate(source.getDtUpdate());
        report.setDescription(source.getDescription());
        report.setStatus(Status.valueOf(source.getStatus()));
        report.setType(Type.valueOf(source.getType()));
        report.setToDate(source.getToDate());
        report.setFromDate(source.getFromDate());
        report.setKey(source.getKey());
        report.setNick(source.getNick());
        return report;
    }

    @Override
    public <U> Converter<ReportEntity, U> andThen(Converter<? super Report, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
