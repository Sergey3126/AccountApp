package com.example.AccountService.controllers.rest;


import com.example.AccountService.models.Account;
import com.example.AccountService.models.User;
import com.example.AccountService.services.api.IAccountService;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final IAccountService accountService;


    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Создает счет
     *
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта), nick(ник), key(токен)
     * @return созданный счет
     */
    @PostMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account createAccount(@RequestBody Account accountRaw) {
        return accountService.createAccount(accountRaw);
    }

    /**
     * Дает список счетов по номеру страницы и ее размеру
     * @param user тело авторизации с nick(ник) и key(токен)
     * @param page номер страницы (больше 0)
     * @param size кол-во объектов на странице(размер страницы, больше 0)
     * @return список счетов
     */
    @GetMapping(value = {"{page}/{size}", "{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<Account> getAccounts(@PathVariable int page, @PathVariable int size, @RequestBody User user) {
        return accountService.getAccounts(page, size, user);
    }

    /**
     * Дает счет по ключу
     * @param uuid Ключ счета
     * @param nick Имя пользователя
     *  @param key Ключ пользователя
     * @return полученный счет
     */
    @GetMapping(value = {"{uuid}/{nick}/{key}", "{uuid}/{nick}/{key}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@PathVariable UUID uuid, @PathVariable String nick, @PathVariable String key) {
        return accountService.getAccount(uuid, nick, key);
    }

    /**
     * Обновляет информацию об счете
     *
     * @param uuid       Ключ счета
     * @param dtUpdate  последняя дата обновления счета
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта), nick(ник), key(токен)
     * @return обновленный счет
     */
    @PutMapping(value = {"{uuid}/dt_update/{dt_update}", "{uuid}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@PathVariable UUID uuid, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate, @RequestBody Account accountRaw) {
        return accountService.updateAccount(uuid, dtUpdate, accountRaw);
    }
}



