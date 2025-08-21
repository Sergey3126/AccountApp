package com.example.AccountService.controllers.rest;


import com.example.AccountService.dao.entity.OperationEntity;

import com.example.AccountService.models.Operation;

import com.example.AccountService.models.User;
import com.example.AccountService.services.api.IOperationService;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/account")
public class OperationController {
    private final IOperationService operationService;


    public OperationController(IOperationService operationsService) {
        this.operationService = operationsService;
    }

    /**
     * Cоздает операцию счета
     *
     * @param accountUuid  Ключ счета
     * @param operationRaw тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат)
     * @return созданную операцию
     */
    @PostMapping(value = {"{account_uuid}/operation", "{account_uuid}/operation/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Operation createOperation(@PathVariable("account_uuid") UUID accountUuid, @RequestBody Operation operationRaw) {
        return operationService.createOperation(accountUuid, operationRaw);
    }

    /**
     * Дает операции счета
     *
     * @param accountUuid Ключ счета
     * @param page        номер страницы(больше 0)
     * @param size        кол-во объектов на странице(размер страницы, больше 0)
     * @param user тело авторизации с nick(ник) и key(токен)
     * @return список операций
     */
    @GetMapping(value = {"{account_uuid}/operation/{page}/{size}", "{account_uuid}/operation/{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<Operation> getOperation(@PathVariable("account_uuid") UUID accountUuid, @PathVariable int page, @PathVariable int size, @RequestBody User user) {
        return operationService.getOperation(accountUuid, page, size, user);
    }

    /**
     * Изменяет операцию
     *
     * @param accountUuid   Ключ счета
     * @param uuidOperation Ключ операции
     * @param dtUpdate      дата обновления
     * @param operationRaw  тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат), nick(ник), key(токен)
     * @return обновленную операцию
     */
    @PutMapping(value = {"{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}", "{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationEntity updateOperation(@PathVariable("account_uuid") UUID accountUuid, @PathVariable("uuid_operation") UUID uuidOperation, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate, @RequestBody Operation operationRaw
    ) {
        return operationService.updateOperation(accountUuid, uuidOperation, dtUpdate, operationRaw);
    }

    /**
     * Удаляет операцию
     *
     * @param accountUuid   Ключ счета
     * @param uuidOperation Ключ операции
     * @param dtUpdate      дата обновления
     * @param user тело авторизации с nick(ник) и key(токен)
     * @return информацию об удаленной операции
     */
    @DeleteMapping(value = {"{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}", "{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationEntity deleteOperation(@RequestBody User user, @PathVariable("account_uuid") UUID accountUuid, @PathVariable("uuid_operation") UUID uuidOperation, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate) {
        return operationService.deleteOperation(accountUuid, uuidOperation, dtUpdate, user);
    }

    /**
     * Дает список операций счета
     * @param nick Имя пользователя
     *  @param key Ключ пользователя
     * @param accountUuid Ключ счета
     * @return список операций
     */
    @GetMapping(value = {"{account_uuid}/operation/{nick}/{key}", "{account_uuid}/operation/{nick}/{key}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Operation> getOperationList(@PathVariable("account_uuid") UUID accountUuid, @PathVariable String nick, @PathVariable String key) {
        return operationService.getOperationList(accountUuid, nick, key);
    }
}


