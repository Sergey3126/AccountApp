package com.example.AccountService.services;

import com.example.AccountService.dao.api.IOperationStorage;

import com.example.AccountService.dao.entity.OperationEntity;
import com.example.AccountService.models.Operation;
import com.example.AccountService.services.api.IOperationService;
import com.example.AccountService.services.api.ValidationException;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OperationService implements IOperationService {

    private final AccountService accountService;
    private final IOperationStorage operationStorage;
    private final ConversionService conversionService;
    private LocalDateTime localDateTime = LocalDateTime.now();


    public OperationService(AccountService accountService, IOperationStorage operationStorage, ConversionService conversionService) {
        this.accountService = accountService;
        this.operationStorage = operationStorage;
        this.conversionService = conversionService;
    }

    @Override
    public Operation create(UUID accountUuid, Operation operationRaw) {
        //  // Проверяем, что обязательные поля не null
        //  if ((operationRaw.getDescription() == null) || (operationRaw.getCurrency() == null) || (operationRaw.getDate() == null) || (operationRaw.getCategory() == null) || (operationRaw.getValue() == 0)) {
        //      throw new ValidationException("Пустая строка");
        //  }
        //  //Проверяет, совпадают ли типы валют
        //  if (!accountService.check(accountUuid, operationRaw.getCurrency())) {
        //      throw new ValidationException("Currency счета и операции не совпадают ");
        //  }
        check(operationRaw, accountUuid);
        try {
            //создает DtCreate, DtUpdate, Uuid, AccountUuid и обновляет баланс счета
            operationRaw.setUuid(UUID.randomUUID());
            operationRaw.setAccountUuid(accountUuid);
            operationRaw.setDtCreate(localDateTime);
            operationRaw.setDtUpdate(localDateTime);
            accountService.updateBalace(operationRaw.getValue(), operationRaw.getAccountUuid());
            operationStorage.save(conversionService.convert(operationRaw, OperationEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз ");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException("Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратору ");
        }
        return operationRaw;
    }

    @Override
    public PageImpl<Operation> getOperation(UUID accountUuid, int page, int size) {
        // Проверка на положительность значений
        if (page <= 0 || size <= 0) {
            throw new ValidationException("Страница и размер должны быть больше 0");
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
            throw new ValidationException("Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз ");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException("Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратору ");
        }
        // Проверка, что start не выходит за пределы списка
        if (start >= operationList.size()) {
            throw new ValidationException("Количество операций меньше запроса");
        }
        return new PageImpl<>(operationList.subList(start, end), pageable, operationList.size());
    }


    @Override
    public OperationEntity updateOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate, Operation operationRaw) {
        OperationEntity operationEntity = operationStorage.findById(uuidOperation).orElse(null);



        // // Проверяем, что обязательные поля не null
        //  if ((operationRaw.getDescription() == null) || (operationRaw.getCurrency() == null) || (operationRaw.getDate() == null) || (operationRaw.getCategory() == null) || (operationRaw.getValue() == 0)) {
        //      throw new ValidationException("Пустая строка");
        //  }
        // //Проверяет, совпадают ли типы валют
        // if (!accountService.check(accountUuid, operationRaw.getCurrency())) {
        //     throw new ValidationException("Currency счета и операции не совпадают ");
        // }
        // //Проверка на наличие операции с этим uuid
        // if (operationEntity == null) {
        //     throw new ValidationException("Неверный uuid");
        // }
        // //Проверка на свежесть данных
        // if (!(operationEntity.getDtUpdate().equals(dtUpdate))) {
        //     throw new ValidationException("Устаревшие данные");
        // }
        // //Проверяет, совпадает ли счет с операцией
        // if (!operationEntity.getAccountUuid().equals(accountUuid)) {
        //     throw new ValidationException("счет не соответствует операции");
        // }

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
            accountService.updateBalace(num, accountUuid);
            operationStorage.save(operationEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз ");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException("Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратору ");
        }
        return operationEntity;
    }

    public OperationEntity deleteOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate) {
        OperationEntity operationEntity = operationStorage.findById(uuidOperation).orElse(null);

        // //Проверка на наличие операции с этим uuid
        // if (operationEntity == null) {
        //     throw new ValidationException("Неверный uuid");
        // }
        // //Проверка на свежесть данных
        // if (!(operationEntity.getDtUpdate().equals(dtUpdate))) {
        //     throw new ValidationException("Устаревшие данные");
        // }
        // //Проверяет, совпадает ли счет с операцией
        // if (!operationEntity.getAccountUuid().equals(accountUuid)) {
        //     throw new ValidationException("счет не соответствует операции");
        // }

        
        checkData(operationEntity, dtUpdate, accountUuid);
        try {
//Обновляет баланс и удаляет операцию
            accountService.updateBalace(-operationEntity.getValue(), accountUuid);
            operationStorage.delete(operationEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз ");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException("Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратору ");
        }
        return operationEntity;
    }


    @Override
    public void checkData(OperationEntity operationEntity, LocalDateTime dtUpdate, UUID accountUuid) {
        //Проверка на наличие операции с этим uuid
        if (operationEntity == null) {
            throw new ValidationException("Неверный uuid");
        }
        //Проверка на свежесть данных
        if (!(operationEntity.getDtUpdate().equals(dtUpdate))) {
            throw new ValidationException("Устаревшие данные");
        }
        //Проверяет, совпадает ли счет с операцией
        if (!operationEntity.getAccountUuid().equals(accountUuid)) {
            throw new ValidationException("счет не соответствует операции");
        }
    }

    @Override
    public void check(Operation operationRaw, UUID accountUuid) {
        // Проверяем, что обязательные поля не null
        if ((operationRaw.getDescription() == null) || (operationRaw.getCurrency() == null) || (operationRaw.getDate() == null) || (operationRaw.getCategory() == null) || (operationRaw.getValue() == 0)) {
            throw new ValidationException("Пустая строка");
        }
        //Проверяет, совпадают ли типы валют
        if (!accountService.checkAccount(accountUuid, operationRaw.getCurrency())) {
            throw new ValidationException("Currency счета и операции не совпадают ");
        }
    }

}
