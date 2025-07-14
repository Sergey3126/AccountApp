package com.example.AccountService.controllers.rest;


import com.example.AccountService.dao.entity.OperationEntity;

import com.example.AccountService.models.Operation;

import com.example.AccountService.services.api.IOperationService;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/account")
public class OperationController {
    private final IOperationService operationService;


    public OperationController(IOperationService operationsService) {
        this.operationService = operationsService;
    }

    /**
     * Создает операцию для счета
     *
     * @param account_uuid Ключ счета
     * @param operationRaw тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат)
     * @return созданную операцию
     */
    @PostMapping(value = {"{account_uuid}/operation", "{account_uuid}/operation/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Operation createOperation(@PathVariable UUID account_uuid, @RequestBody Operation operationRaw) {
        return operationService.createOperation(account_uuid, operationRaw);
    }

    /**
     * Дает операции счета
     *
     * @param account_uuid Ключ счета
     * @param page         номер страницы
     * @param size         кол-во объектов на странице(размер страницы)
     * @return список операций
     */
    @GetMapping(value = {"{account_uuid}/operation/{page}/{size}", "{account_uuid}/operation/{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<Operation> getOperation(@PathVariable UUID account_uuid, @PathVariable int page, @PathVariable int size) {
        return operationService.getOperation(account_uuid, page, size);
    }

    /**
     * Изменяет операцию
     *
     * @param account_uuid   Ключ счета
     * @param uuid_operation Ключ операции
     * @param dtUpdate       дата обновления
     * @param operationRaw   тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат)
     * @return обновленный счет
     */
    @PutMapping(value = {"{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}", "{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationEntity updateOperation(@PathVariable UUID account_uuid, @PathVariable UUID uuid_operation, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate, @RequestBody Operation operationRaw) {
        return operationService.updateOperation(account_uuid, uuid_operation, dtUpdate, operationRaw);
    }

    /**
     * Удаляет операцию
     *
     * @param account_uuid   Ключ счета
     * @param uuid_operation Ключ операции
     * @param dtUpdate       дата обновления
     * @return информацию об удаленном счете
     */
    @DeleteMapping(value = {"{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}", "{account_uuid}/operation/{uuid_operation}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationEntity deleteOperation(@PathVariable UUID account_uuid, @PathVariable UUID uuid_operation, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate) {
        return operationService.deleteOperation(account_uuid, uuid_operation, dtUpdate);
    }
}


