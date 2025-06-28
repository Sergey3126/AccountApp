package com.example.AccountService.controllers.rest;


import com.example.AccountService.dao.entity.OperationEntity;
import com.example.AccountService.models.Account;
import com.example.AccountService.models.Operation;
import com.example.AccountService.services.api.IAccountService;
import com.example.AccountService.services.api.IOperationService;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/")
public class OperationController {
    private final IOperationService operationService;


    public OperationController(IOperationService operationsService) {
        this.operationService = operationsService;
    }


    @PostMapping(value = {"account/{accountUuid}/operation", "account/{accountUuid}/operation/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Operation create(@PathVariable UUID accountUuid, @RequestBody Operation operation) {
        return operationService.create(accountUuid, operation);
    }


    @GetMapping(value = {"account/{accountUuid}/operation/{page}/{size}", "account/{accountUuid}/operation/{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<Operation> getOperation(@PathVariable UUID accountUuid, @PathVariable int page, @PathVariable int size) {
        return operationService.getOperation(accountUuid, page, size);
    }


    @PutMapping(value = {"account/{accountUuid}/operation/{uuidOperation}/dt_update/{dt_update}", "account/{accountUuid}/operation/{uuidOperation}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationEntity updateOperation(@PathVariable UUID accountUuid, @PathVariable UUID uuidOperation, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate, @RequestBody Operation operation) {
        return operationService.updateOperation(accountUuid, uuidOperation, dtUpdate, operation);
    }

    @DeleteMapping(value = {"account/{accountUuid}/operation/{uuidOperation}/dt_update/{dt_update}", "account/{accountUuid}/operation/{uuidOperation}/dt_update/{dt_update}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationEntity deleteOperation(@PathVariable UUID accountUuid, @PathVariable UUID uuidOperation, @PathVariable("dt_update") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtUpdate) {
        return operationService.deleteOperation(accountUuid, uuidOperation, dtUpdate);
    }
}


