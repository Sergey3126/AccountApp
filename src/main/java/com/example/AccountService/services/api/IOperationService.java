package com.example.AccountService.services.api;

import com.example.AccountService.dao.entity.AccountEntity;
import com.example.AccountService.dao.entity.OperationEntity;
import com.example.AccountService.models.Account;
import com.example.AccountService.models.Operation;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IOperationService {
    /**
     * создает операцию для счета
     *
     * @param accountUuid  индификатор счета
     * @param operationRaw тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат)
     * @return созданую операцию
     */
    Operation create(UUID accountUuid, Operation operationRaw);

    /**
     * дает операции аккауна
     *
     * @param accountUuid индификатор счета
     * @param page        номер страницы
     * @param size        кол-во обЪектов на странице(размер страницы)
     * @return список операций
     */
    PageImpl<Operation> getOperation(UUID accountUuid, int page, int size);

    /**
     * изменяет операцию
     *
     * @param accountUuid   индификатор счета
     * @param uuidOperation индификатор операции
     * @param dtUpdate      дата обновления
     * @param operationRaw  тело операции с date(дата операции), description(описание), value(значение изменения счета), currency(валюта), category(категория трат)
     * @return обновленный аккаунт
     */
    OperationEntity updateOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate, Operation operationRaw);

    /**
     * удаляет операцию
     *
     * @param accountUuid   индификатор счета
     * @param uuidOperation индификатор операции
     * @param dtUpdate      дата обновления
     * @return информацию об удаленном аккаунте
     */ 
    OperationEntity deleteOperation(UUID accountUuid, UUID uuidOperation, LocalDateTime dtUpdate);

    /**
     * проверка ошибок: свежесть данных, наличие операции и правильность счета
     *
     * @param operationEntity обЪект БД
     * @param dtUpdate        дфта последнего обновления operationEntity
     * @param accountUuid     индификатор счета
     */
    void checkData(OperationEntity operationEntity, LocalDateTime dtUpdate, UUID accountUuid);

    /**
     * проверка ошибок: null и совпадение валют
     *
     * @param operationRaw Тело, которое передали в запросе
     * @param accountUuid  индификатор счета
     */
    void check(Operation operationRaw, UUID accountUuid);
}
