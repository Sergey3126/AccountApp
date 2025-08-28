package com.example.AccountService.services.api;

import com.example.AccountService.dao.entity.OperationEntity;
import com.example.AccountService.models.Operation;
import com.example.AccountService.models.User;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
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
     * @param user тело авторизации с nick(ник) и key(токен)
     * @return список операций
     */
    PageImpl<Operation> getOperation(UUID accountUuid, int page, int size, User user);

    /**
     * Изменяет операцию
     *
     * @param accountUuid   Ключ счета
     * @param uuidOperation Ключ операции
     * @param dtUpdate      дата обновления
     * @param operationRaw  тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат), nick(ник), key(токен)
     * @return обновленную операцию
     */
    OperationEntity updateOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate, Operation operationRaw);

    /**
     * Удаляет операцию
     *
     * @param accountUuid   Ключ счета
     * @param uuidOperation Ключ операции
     * @param dtUpdate      дата обновления
     * @param user тело авторизации с nick(ник) и key(токен)
     * @return информацию об удаленной операции
     */
    OperationEntity deleteOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate, User user);

    /**
     * Дает список операций счета
     * @param nick Имя пользователя
     *  @param key Ключ пользователя
     * @param accountUuid Ключ счета
     * @return список операций
     */

    List<Operation> getOperationList(UUID accountUuid, String nick, String key);
}
