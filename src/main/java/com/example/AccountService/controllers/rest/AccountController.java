package com.example.AccountService.controllers.rest;


import com.example.AccountService.models.Account;
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
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта)
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
     *
     * @param page номер страницы
     * @param size кол-во объектов на странице(размер страницы)
     * @return список счетов
     */
    @GetMapping(value = {"{page}/{size}", "{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<Account> getAccounts(@PathVariable int page, @PathVariable int size) {
        return accountService.getAccounts(page, size);
    }

    /**
     * Дает счет по uuid
     *
     * @param uuid Ключ счета
     * @return полученный счет
     */
    @GetMapping(value = {"{uuid}", "{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@PathVariable UUID uuid) {
        return accountService.getAccount(uuid);
    }

    /**
     * Обновляет информацию об аккаунте
     *
     * @param uuid       Ключ счета
     * @param dtUpdate  последняя дата обновления счета
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта)
     * @return обновленный аккаунт
     */
    @PutMapping(value = {"{uuid}/dt_update/{dt_update}", "{uuid}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@PathVariable UUID uuid, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate, @RequestBody Account accountRaw) {
        return accountService.updateAccount(uuid, dtUpdate, accountRaw);
    }


}


