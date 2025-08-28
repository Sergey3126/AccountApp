package com.example.UserService.services.api;


import com.example.UserService.models.User;

public interface IUserService {


    User createUser(User user);

    User authorizationUser(User user);
}
