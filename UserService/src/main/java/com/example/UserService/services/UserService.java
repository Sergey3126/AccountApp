package com.example.UserService.services;


import com.example.UserService.dao.api.IUserStorage;
import com.example.UserService.dao.entity.UserEntity;
import com.example.UserService.models.User;
import com.example.UserService.services.api.IUserService;

import org.springframework.core.convert.ConversionService;

import com.example.UserService.services.api.MessageError;
import com.example.UserService.services.api.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final EncryptionService encryptionService;
    private final ConversionService conversionService;
    private final IUserStorage userStorage;


    public UserService(EncryptionService encryptionService, ConversionService conversionService, IUserStorage userStorage) {
        this.encryptionService = encryptionService;
        this.conversionService = conversionService;
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        if (userStorage.existsByNick(user.getNick())) {
            throw new ValidationException(MessageError.NICK_TAKEN);
        }
        if (!user.getPassword().matches("^[a-zA-Z]+$")) {
            throw new ValidationException(MessageError.PASSWORD_ERROR);
        }
        check(user);
        try {
            //создает Uuid

            user.setUuid(UUID.randomUUID());
            userStorage.save(conversionService.convert(user, UserEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return user;
    }


    @Override
    public User authorizationUser(User user) {

        check(user);
        try {
            //получает ключ
            if (Objects.equals(user.getPassword(), userStorage.findByNick(user.getNick()).getPassword())) {
                user.setUuid(userStorage.findByNick(user.getNick()).getUuid());
                user.setKey(encryptionService.encrypt(user.getNick()));
            }else {
                throw new ValidationException(MessageError.PASSWORD_BAD);
            }
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return user;
    }


    private void check(User user) {
        // Проверяем, что обязательные поля не пусты
        if ((user.getNick() == null) || (user.getPassword() == null)) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }

    }


}

