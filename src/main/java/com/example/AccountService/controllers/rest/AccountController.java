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
@RequestMapping("/api/v1/")
public class AccountController {
    private final IAccountService accountService;


    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping(value = {"account", "account/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account createAccount(@RequestBody Account account) {
        return accountService.create(account);
    }


    @GetMapping(value = {"account/{page}/{size}", "account/{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<Account> getAccounts(@PathVariable int page, @PathVariable int size) {
        return accountService.getAccounts(page, size);
    }


    @GetMapping(value = {"account/{uuid}", "account/{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@PathVariable UUID uuid) {
        return accountService.getAccount(uuid);
    }

    @PutMapping(value = {"account/{uuid}/dt_update/{dt_update}", "account/{uuid}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@PathVariable UUID uuid, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate, @RequestBody Account account) {
        return accountService.updateAccount(uuid, dtUpdate, account);
    }


}


