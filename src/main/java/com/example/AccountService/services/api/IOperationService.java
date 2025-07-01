package com.example.AccountService.services.api;

import com.example.AccountService.dao.entity.OperationEntity;
import com.example.AccountService.models.Operation;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IOperationService {
    /**
     * Cоздает операцию счета
     *
     * @param accountUuid  Ключ счета
     * @param operationRaw тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат)
     * @return созданную операцию
     */
    Operation createOperation(UUID accountUuid, Operation operationRaw);

    /**
     * Дает операции счета
     *
     * @param accountUuid Ключ счета
     * @param page        номер страницы(больше 0)
     * @param size        кол-во объектов на странице(размер страницы, больше 0)
     * @return список операций
     */
    PageImpl<Operation> getOperation(UUID accountUuid, int page, int size);

    /**
     * Изменяет операцию
     *
     * @param accountUuid   Ключ счета
     * @param uuidOperation Ключ операции
     * @param dtUpdate      дата обновления
     * @param operationRaw  тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат)
     * @return обновленную операцию
     */
    OperationEntity updateOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate, Operation operationRaw);

    /**
     * Удаляет операцию
     *
     * @param accountUuid   Ключ счета
     * @param uuidOperation Ключ операции
     * @param dtUpdate      дата обновления
     * @return информацию об удаленной операции
     */
    OperationEntity deleteOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate);

}
