package com.example.ClassifierService.services;

import com.example.ClassifierService.dao.api.ICurrencyStorage;
import com.example.ClassifierService.dao.entity.CurrencyEntity;
import com.example.ClassifierService.models.Currency;
import com.example.ClassifierService.services.api.ICurrencyService;
import com.example.ClassifierService.services.api.MessageError;
import com.example.ClassifierService.services.api.ValidationException;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CurrencyService implements ICurrencyService {
    private final ICurrencyStorage currencyStorage;
    private final ConversionService conversionService;
    private LocalDateTime localDateTime = LocalDateTime.now();

    public CurrencyService(ICurrencyStorage currencyStorage, ConversionService conversionService) {
        this.currencyStorage = currencyStorage;
        this.conversionService = conversionService;
    }


    @Override
    public Currency createCurrency(Currency currencyRaw) {

        // Проверяем, что обязательные поля не пусты
        if (currencyRaw.getTitle() == null) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }
        //Проверка свободен ли такой title
        if (currencyStorage.existsByTitle(currencyRaw.getTitle())) {
            throw new ValidationException(MessageError.TITLE_TAKEN);
        }
        try {
            //создает DtCreate, DtUpdate, Uuid
            currencyRaw.setDtCreate(localDateTime);
            currencyRaw.setDtUpdate(localDateTime);
            currencyRaw.setUuid(UUID.randomUUID());
            currencyStorage.save(conversionService.convert(currencyRaw, CurrencyEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return currencyRaw;
    }


    @Override
    public PageImpl<Currency> getCurrencies(int page, int size) {
        // Проверка на положительность значений(что больше 0)
        if (page <= 0) {
            throw new ValidationException(MessageError.PAGE_NUMBER);
        }
        if (size <= 0) {
            throw new ValidationException(MessageError.PAGE_SIZE);
        }
        int start;
        List<Currency> currencyList;
        int end;
        Pageable pageable;
        try {
            List<CurrencyEntity> currencyEntityList = currencyStorage.findAll();
            currencyList = new ArrayList<>();
            pageable = Pageable.ofSize(size).withPage(page - 1);
            // Конвертация CurrencyEntity в Currency и добавление в список
            for (int i = 0; i < currencyEntityList.size(); i++) {
                CurrencyEntity currencyEntity = currencyEntityList.get(i);
                Currency currency = conversionService.convert(currencyEntity, Currency.class);
                currencyList.add(currency);
            }
            //Вычисление индексов start и end для страниц
            start = (int) pageable.getOffset();
            end = Math.min((start + pageable.getPageSize()), currencyList.size());
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        // Проверка, что start не выходит за пределы списка
        if (start >= currencyList.size()) {
            throw new ValidationException(MessageError.RETRIEVE_OBJECT);
        }
        return new PageImpl<>(currencyList.subList(start, end), pageable, currencyList.size());
    }

    @Override
    public Currency getCurrency(UUID uuid) {
        Currency currency;
        CurrencyEntity currencyEntity;
        try {
            //получение валюты
            currencyEntity = currencyStorage.findById(uuid).orElse(null);
            currency = conversionService.convert(currencyEntity, Currency.class);

        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        //Проверка, что такая валюта есть
        if (currency == null) {
            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
        return currency;
    }


    @Override
    public String getCurrencyTitle(UUID uuid) {
        //получает валюту и возвращает название
        CurrencyEntity currencyEntity = currencyStorage.findById(uuid).orElse(null);
        Currency currency = conversionService.convert(currencyEntity, Currency.class);
        return currency.getTitle();
    }


}




