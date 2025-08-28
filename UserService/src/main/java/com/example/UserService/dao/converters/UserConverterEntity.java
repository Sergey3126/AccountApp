package com.example.UserService.dao.converters;


import com.example.UserService.dao.entity.UserEntity;
import com.example.UserService.models.User;
import org.springframework.core.convert.converter.Converter;


public class UserConverterEntity implements Converter<User, UserEntity> {


    @Override
    public UserEntity convert(User source) {


        UserEntity operationEntity = new UserEntity();

        operationEntity.setUuid(source.getUuid());
        operationEntity.setNick(source.getNick());
        operationEntity.setPassword(source.getPassword());
        return operationEntity;
    }

    @Override
    public <U> Converter<User, U> andThen(Converter<? super UserEntity, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
