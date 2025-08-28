package com.example.ClassifierService.services.api;

import com.example.ClassifierService.models.Currency;
import org.springframework.data.domain.PageImpl;

import java.util.UUID;

public interface ICurrencyService {

    /**
     * Создает валюту
     *
     * @param currencyRaw тело валюты с title(название)
     * @return созданную валюту
     */
    Currency createCurrency(Currency currencyRaw);


    /**
     * Дает список валют по номеру страницы и ее размеру
     *
     * @param page номер страницы (больше 0)
     * @param size кол-во объектов на странице(размер страницы, больше 0)
     * @return список валют
     */
    PageImpl<Currency> getCurrencies(int page, int size);


    /**
     * Дает валюту по ключу
     *
     * @param uuid ключ валюты
     * @return валюту
     */
    Currency getCurrency(UUID uuid);

    /**
     * Дает название валюты
     *
     * @param uuid ключ валюты
     * @return название валюты
     */
    String getCurrencyTitle(UUID uuid);
}
