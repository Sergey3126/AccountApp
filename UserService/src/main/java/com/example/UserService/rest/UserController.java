package com.example.UserService.rest;


import com.example.UserService.models.User;
import com.example.UserService.services.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final IUserService userService;


    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Создает пользователя
     * @param user тело авторизации с nick(ник),uuid,password(пароль) и key(токен)

     * @return пользователя
     */
    @PostMapping(value = {"create", "create/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Дает пользователя
     * @param user тело авторизации с nick(ник),uuid,password(пароль) и key(токен)
     * @return пользователь
     */
    @GetMapping(value = {"authorization", "authorization/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public User authorization(@RequestBody User user) {
        return userService.authorizationUser(user);
    }
}



