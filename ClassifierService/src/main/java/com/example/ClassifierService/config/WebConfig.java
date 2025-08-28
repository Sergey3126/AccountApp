package com.example.ClassifierService.config;


import com.example.ClassifierService.dao.converters.CurrencyConverter;
import com.example.ClassifierService.dao.converters.CurrencyConverterEntity;
import com.example.ClassifierService.dao.converters.OperationCategoryConverter;
import com.example.ClassifierService.dao.converters.OperationCategoryConverterEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OperationCategoryConverter());
        registry.addConverter(new OperationCategoryConverterEntity());
        registry.addConverter(new CurrencyConverterEntity());
        registry.addConverter(new CurrencyConverter());
    }
}