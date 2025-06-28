package com.example.AccountService.services.api;

import com.example.AccountService.models.Account;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.UUID;


public interface IAccountService {
    /**
     * создает счет
     *
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта)
     * @return созданный счет
     */
    Account create(Account accountRaw);

    /**
     * дает список счетов по номеру страницы  и ее размеру
     *
     * @param page номер страницы
     * @param size кол-во обЪектов на странице(размер страницы)
     * @return список счетов
     */
    PageImpl<Account> getAccounts(int page, int size);

    /**
     * дает счет по uuid
     *
     * @param uuid индификатор счета
     * @return полученный счет
     */
    Account getAccount(UUID uuid);

    /**
     * обновляет информацию об аккаунте
     *
     * @param uuid       индификатор счета
     * @param dt_update  последняя дата обновления счета
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта)
     * @return обновленный аккаунт
     */
    Account updateAccount(UUID uuid, LocalDateTime dt_update, Account accountRaw);

    /**
     * проверяет совпадают ли типы валют
     *
     * @param accountUuid индификатор счета
     * @param currency    тип валют
     * @return совпадает или нет
     */

    boolean checkAccount(UUID accountUuid, UUID currency);

    /**
     * Обновляет баланс
     *
     * @param value       значение на которое меняется баланс
     * @param accountUuid индификатор счета
     */
    void updateBalace(int value, UUID accountUuid);

    /**
     * проверка ошибок: null и свободен ли title
     *
     * @param accountRaw Тело, которое передали в запросе
     */
    void check(Account accountRaw);
}
