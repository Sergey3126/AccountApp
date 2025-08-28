package com.example.AccountService.services.api;

import com.example.AccountService.models.Account;
import com.example.AccountService.models.User;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.UUID;


public interface IAccountService {
    /**
     * Создает счет
     *
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта), nick(ник), key(токен)
     * @return созданный счет
     */
    Account createAccount(Account accountRaw);

    /**
     * Дает список счетов по номеру страницы и ее размеру
     * @param user тело авторизации с nick(ник) и key(токен)
     * @param page номер страницы (больше 0)
     * @param size кол-во объектов на странице(размер страницы, больше 0)
     * @return список счетов
     */
    PageImpl<Account> getAccounts(int page, int size, User user);

    /**
     * Дает счет по ключу
     * @param nick Имя пользователя
     *  @param key Ключ пользователя
     * @param uuid Ключ счета
     * @return полученный счет
     */

    Account getAccount(UUID uuid, String nick, String key);

    /**
     * Обновляет информацию об счете
     *
     * @param uuid       Ключ счета
     * @param dt_update  последняя дата обновления счета
     * @param accountRaw тело счета с title(название), description(описание), type(тип), currency(валюта), nick(ник), key(токен)
     * @return обновленный счет
     */
    Account updateAccount(UUID uuid, LocalDateTime dt_update, Account accountRaw);


}
