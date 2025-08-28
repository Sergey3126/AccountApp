package com.example.AccountService.config;


import com.example.AccountService.dao.converters.AccountConverter;
import com.example.AccountService.dao.converters.AccountConverterEntity;
import com.example.AccountService.dao.converters.OperationConverter;
import com.example.AccountService.dao.converters.OperationConverterEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new AccountConverter());
        registry.addConverter(new AccountConverterEntity());
        registry.addConverter(new OperationConverter());
        registry.addConverter(new OperationConverterEntity());
    }
}