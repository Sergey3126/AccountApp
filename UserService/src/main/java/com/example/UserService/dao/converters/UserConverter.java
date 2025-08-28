package com.example.UserService.dao.converters;


import com.example.UserService.dao.entity.UserEntity;
import com.example.UserService.models.User;
import org.springframework.core.convert.converter.Converter;


public class UserConverter implements Converter<UserEntity, User> {


    @Override
    public User convert(UserEntity source) {
        User account = new User();
        account.setUuid(source.getUuid());
        account.setNick(source.getNick());
        account.setPassword(source.getPassword());
        return account;
    }

    @Override
    public <U> Converter<UserEntity, U> andThen(Converter<? super User, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
