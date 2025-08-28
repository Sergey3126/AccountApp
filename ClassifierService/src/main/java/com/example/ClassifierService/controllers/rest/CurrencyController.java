package com.example.ClassifierService.controllers.rest;

import com.example.ClassifierService.models.Currency;
import com.example.ClassifierService.services.api.ICurrencyService;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/classifier/currency")
public class CurrencyController {
    private final ICurrencyService currencyService;

    public CurrencyController(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    /**
     * Создает валюту
     *
     * @param currencyRaw тело валюты с title(название)
     * @return созданную валюту
     */
    @PostMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Currency createCurrency(@RequestBody Currency currencyRaw) {
        return currencyService.createCurrency(currencyRaw);
    }

    /**
     * Дает список валют по номеру страницы и ее размеру
     *
     * @param page номер страницы(больше 0)
     * @param size кол-во объектов на странице(размер страницы(больше 0))
     * @return список валют
     */
    @GetMapping(value = {"{page}/{size}", "{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<Currency> getCurrencies(@PathVariable int page, @PathVariable int size) {
        return currencyService.getCurrencies(page, size);
    }

    /**
     * Дает валюту по ключу
     *
     * @param uuid ключ валюты
     * @return валюту
     */
    @GetMapping(value = {"{uuid}", "{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Currency getCurrency(@PathVariable UUID uuid) {
        return currencyService.getCurrency(uuid);
    }

    /**
     * Дает название валюты
     *
     * @param uuid ключ валюты
     * @return название валюты
     */
    @GetMapping(value = {"{uuid}/title", "{uuid}/title/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getCurrencyTitle(@PathVariable UUID uuid) {
        return currencyService.getCurrencyTitle(uuid);
    }
}
