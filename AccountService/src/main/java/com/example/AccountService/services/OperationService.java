package com.example.AccountService.services;

import com.example.AccountService.dao.api.IOperationStorage;

import com.example.AccountService.dao.entity.OperationEntity;

import com.example.AccountService.models.Operation;
import com.example.AccountService.models.User;
import com.example.AccountService.services.api.IOperationService;
import com.example.AccountService.services.api.MessageError;
import com.example.AccountService.services.api.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


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
    private ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate restTemplate = new RestTemplate();

    //ссылка для доступа к списку валют
    @Value("${classifier_currency_url}")
    private String currencyUrl;
    //ссылка для доступа к списку категорий
    @Value("${classifier_category_url}")
    private String categoryUrl;
    //ссылка для доступа к шифровке
    @Value("${encryption_url}")
    private String encryptionUrl;


    public OperationService(AccountService accountService, IOperationStorage operationStorage, ConversionService conversionService) {
        this.accountService = accountService;
        this.operationStorage = operationStorage;
        this.conversionService = conversionService;

    }

    @Override
    public Operation createOperation(UUID accountUuid, Operation operationRaw) {

        User user = new User();
        user.setKey(operationRaw.getKey());
        user.setNick(operationRaw.getNick());
        checkKey(user);

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
    public PageImpl<Operation> getOperation(UUID accountUuid, int page, int size, User user) {
        checkKey(user);
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
            //Получаем доступные операции и удаляем лишнее
            List<OperationEntity> operationEntityList = operationStorage.findByNick(user.getNick());
            for (int e = operationEntityList.size() - 1; e >= 0; e--) {
                if (operationEntityList.get(e).getAccountUuid() != accountUuid) {
                    operationEntityList.remove(e);
                }
            }
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

        User user = new User();
        user.setKey(operationRaw.getKey());
        user.setNick(operationRaw.getNick());
        checkKey(user);

        check(operationRaw, accountUuid);

        OperationEntity operationEntity = new OperationEntity();
        //Получаем доступные операции и ищем нужную
        List<OperationEntity> operationEntityList = operationStorage.findByNick(operationRaw.getNick());
        for (int i = 0; i < operationEntityList.size(); i++) {
            operationEntity = operationEntityList.get(i);
            if (operationEntity.getUuid() == uuidOperation) {
                break;
            }
        }
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

    @Override
    public OperationEntity deleteOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate, User user) {


        checkKey(user);
        OperationEntity operationEntity = new OperationEntity();
        //Получаем доступные операции и ищем нужную
        List<OperationEntity> operationEntityList = operationStorage.findByNick(user.getNick());
        for (int i = 0; i < operationEntityList.size(); i++) {
            operationEntity = operationEntityList.get(i);
            if (operationEntity.getUuid() == uuidOperation) {
                break;
            }
        }
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

    @Override
    public List<Operation> getOperationList(UUID accountUuid, String nick, String key) {
        int start;
        List<Operation> operationList = new ArrayList<>();
        int end;
        Pageable pageable;

        User user = new User();
        user.setNick(nick);
        user.setKey(key);
        checkKey(user);

        try {
            //Получаем доступные операции и удаляем лишнее
            List<OperationEntity> operationEntityList = operationStorage.findByNick(nick);
            for (int e = operationEntityList.size() - 1; e >= 0; e--) {
                if (operationEntityList.get(e).getAccountUuid() != accountUuid) {
                    operationEntityList.remove(e);
                }
            }

            // Конвертация OperationEntity в Operation и добавление в список
            for (int i = 0; i < operationEntityList.size(); i++) {
                OperationEntity operationEntity = operationEntityList.get(i);
                Operation operation = conversionService.convert(operationEntity, Operation.class);
                operationList.add(operation);
            }
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return operationList;
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
        User user = new User();
        user.setKey(operationRaw.getKey());
        user.setNick(operationRaw.getNick());
        //Проверяет, совпадают ли типы валют
        if (!accountService.checkAccount(accountUuid, operationRaw.getCurrency())) {
            throw new ValidationException(MessageError.INCORRECT_CURRENCY);
        }
        checkAccessibility(operationRaw, categoryUrl, String.valueOf(operationRaw.getCategory()));
        checkAccessibility(operationRaw, currencyUrl, String.valueOf(operationRaw.getCurrency()));
    }


    //проверят доступность категории или валюты
    private void checkAccessibility(Operation operationRaw, String url, String uuid) {


        try (InputStream stream = new URL(url + uuid).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String str = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {

            throw new ValidationException(MessageError.INCORRECT_UUID);

        }
    }

    //проверка авторизации
    private void checkKey(User user) {
        // Проверяем, что обязательные поля не пусты
        if (user.getNick() == null || user.getKey() == null) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }
        try {
            //получаем совпадает ли токен
            String jsonUser = objectMapper.writeValueAsString(user);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> str = new HttpEntity<>(jsonUser, headers);
            ResponseEntity<Boolean> response = restTemplate.postForEntity(encryptionUrl + "check", str, boolean.class);
            boolean bool = response.getBody();

            if (!bool) {
                throw new ValidationException(MessageError.INCORRECT_TOKEN);
            }
        } catch (IOException e) {
            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
    }
}

