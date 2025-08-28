package com.example.UserService.config;



import com.example.UserService.dao.converters.UserConverter;
import com.example.UserService.dao.converters.UserConverterEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new UserConverter());
        registry.addConverter(new UserConverterEntity());
    }
}