package com.example.AccountService.services;

import com.example.AccountService.dao.api.IOperationStorage;

import com.example.AccountService.dao.entity.OperationEntity;

import com.example.AccountService.models.Operation;
import com.example.AccountService.services.api.IOperationService;
import com.example.AccountService.services.api.MessageError;
import com.example.AccountService.services.api.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OperationService implements IOperationService {

    private final AccountService accountService;
    private final IOperationStorage operationStorage;
    private final ConversionService conversionService;
    private LocalDateTime localDateTime = LocalDateTime.now();

    //ссылка для доступа к списку валют
    @Value("${classifier_currency_url}")
    private String currencyUrl;
    //ссылка для доступа к списку категорий
    @Value("${classifier_category_url}")
    private String categoryUrl;

    public OperationService(AccountService accountService, IOperationStorage operationStorage, ConversionService conversionService) {
        this.accountService = accountService;

        this.operationStorage = operationStorage;
        this.conversionService = conversionService;

    }

    @Override
    public Operation createOperation(UUID accountUuid, Operation operationRaw) {

        check(operationRaw, accountUuid);
        try {
            //создает DtCreate, DtUpdate, Uuid, AccountUuid и обновляет баланс счета
            operationRaw.setUuid(UUID.randomUUID());
            operationRaw.setAccountUuid(accountUuid);
            operationRaw.setDtCreate(localDateTime);
            operationRaw.setDtUpdate(localDateTime);
            accountService.updateBalance(operationRaw.getValue(), operationRaw.getAccountUuid());
            operationStorage.save(conversionService.convert(operationRaw, OperationEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return operationRaw;
    }

    @Override
    public PageImpl<Operation> getOperation(UUID accountUuid, int page, int size) {
        // Проверка на положительность значений(что больше 0 и не равен 0)
        if (page <= 0) {
            throw new ValidationException(MessageError.PAGE_NUMBER);
        }
        if (size <= 0) {
            throw new ValidationException(MessageError.PAGE_SIZE);
        }
        int start;
        List<Operation> operationList;
        int end;
        Pageable pageable;
        try {
            List<OperationEntity> operationEntityList = operationStorage.findByAccountUuid(accountUuid);
            operationList = new ArrayList<>();
            pageable = Pageable.ofSize(size).withPage(page - 1);
            // Конвертация OperationEntity в Operation и добавление в список
            for (int i = 0; i < operationEntityList.size(); i++) {
                OperationEntity operationEntity = operationEntityList.get(i);
                Operation operation = conversionService.convert(operationEntity, Operation.class);
                operationList.add(operation);
            }
            //Вычисление индексов start и end для страниц
            start = (int) pageable.getOffset();
            end = Math.min((start + pageable.getPageSize()), operationList.size());

        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        // Проверка, что start не выходит за пределы списка
        if (start >= operationList.size()) {
            throw new ValidationException(MessageError.RETRIEVE_ACCOUNTS);
        }
        return new PageImpl<>(operationList.subList(start, end), pageable, operationList.size());
    }


    @Override
    public OperationEntity updateOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate, Operation operationRaw) {
        OperationEntity operationEntity = operationStorage.findById(uuidOperation).orElse(null);


        check(operationRaw, accountUuid);
        checkData(operationEntity, dtUpdate, accountUuid);

        try {
            //изменение баланса
            int num = operationRaw.getValue() - operationEntity.getValue();
            //Обновляет данные Description, DtUpdate, Category, Currency, Date, Value и сохраняет
            operationEntity.setDate(operationRaw.getDate());
            operationEntity.setDescription(operationRaw.getDescription());
            operationEntity.setCategory(operationRaw.getCategory());
            operationEntity.setValue(operationRaw.getValue());
            operationEntity.setDtUpdate(localDateTime);
            operationEntity.setCurrency(operationRaw.getCurrency());
            accountService.updateBalance(num, accountUuid);
            operationStorage.save(operationEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return operationEntity;
    }

    public OperationEntity deleteOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate) {
        OperationEntity operationEntity = operationStorage.findById(uuidOperation).orElse(null);

        checkData(operationEntity, dtUpdate, accountUuid);
        try {
//Обновляет баланс и удаляет операцию
            accountService.updateBalance(-operationEntity.getValue(), accountUuid);
            operationStorage.delete(operationEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return operationEntity;
    }


    private void checkData(OperationEntity operationEntity, LocalDateTime dtUpdate, UUID accountUuid) {
        //Проверка на наличие операции с этим ключом
        if (operationEntity == null) {
            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
        //Проверка на свежесть данных
        if (!(operationEntity.getDtUpdate().equals(dtUpdate))) {
            throw new ValidationException(MessageError.OUTDATED_DATA);
        }
        //Проверяет, совпадает ли счет с операцией
        if (!operationEntity.getAccountUuid().equals(accountUuid)) {
            throw new ValidationException(MessageError.INCORRECT_OPERATION);
        }
    }


    private void check(Operation operationRaw, UUID accountUuid) {
        // Проверяем, что обязательные поля не пусты
        if ((operationRaw.getDescription() == null) || (operationRaw.getCurrency() == null) || (operationRaw.getDate() == null) || (operationRaw.getCategory() == null) || (operationRaw.getValue() == 0)) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }
        //Проверяет, совпадают ли типы валют
        if (!accountService.checkAccount(accountUuid, operationRaw.getCurrency())) {
            throw new ValidationException(MessageError.INCORRECT_CURRENCY);
        }
        checkCurrency(operationRaw);
        checkOperationCategory(operationRaw);
    }

    //проверят доступен ли такой тип валюты
    private void checkCurrency(Operation operationRaw) {
        String uuid = String.valueOf(operationRaw.getCurrency());

        try (InputStream stream = new URL(currencyUrl + uuid).openStream()) {
            //получает текст
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String currecy = reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {

            throw new ValidationException(MessageError.UUID_CURRENCY);

        }
    }

    //проверят доступен ли такой тип операции
    private void checkOperationCategory(Operation operationRaw) {
        String uuid = String.valueOf(operationRaw.getCategory());

        try (InputStream stream = new URL(categoryUrl + uuid).openStream()) {
            //получает текст
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String category = reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {

            throw new ValidationException(MessageError.UUID_OPERATION);

        }
    }

}

