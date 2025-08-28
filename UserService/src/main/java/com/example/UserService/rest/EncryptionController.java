package com.example.UserService.rest;

import com.example.UserService.models.User;
import com.example.UserService.services.api.IEncryptionService;
import com.example.UserService.services.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/encryption")
public class EncryptionController {
    private final IEncryptionService encryptionService;

    public EncryptionController(IEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }


    /**
     * Шифрует
     * @param user тело авторизации с nick(ник),uuid,password(пароль) и key(токен)
     * @return шифрованный token
     */
    @PostMapping(value = {"encrypt", "encrypt/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String encrypt(@RequestBody User user) {
        return encryptionService.encrypt(user.getNick());
    }

    /**
     * Ди шифрует
     * @param user тело авторизации с nick(ник),uuid,password(пароль) и key(токен)
     * @return  Ди шифрованный token
     */
    @PostMapping(value = {"decrypt", "decrypt/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String decrypt(@RequestBody User user) {
        return encryptionService.decrypt(user.getNick());
    }

    /**
     * Проверяет правильность token
     * @param user тело авторизации с nick(ник),uuid,password(пароль) и key(токен)
     * @return результат
     */
    @PostMapping(value = {"check", "check/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean check(@RequestBody User user) {
        return encryptionService.check(user.getNick(), user.getKey());
    }
}
