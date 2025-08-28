package com.example.MailService.config;


import com.example.MailService.dao.converters.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ReportDataConverter());
        registry.addConverter(new ReportDataConverterEntity());
        registry.addConverter(new AccountConverter());
        registry.addConverter(new AccountConverterEntity());
        registry.addConverter(new CategoryConverter());
        registry.addConverter(new CategoryConverterEntity());
        registry.addConverter(new ReportDataConverter());
        registry.addConverter(new ReportDataConverterEntity());
    }
}