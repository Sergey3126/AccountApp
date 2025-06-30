package com.example.AccountService.services;

import com.example.AccountService.dao.api.IAccountStorage;
import com.example.AccountService.dao.entity.AccountEntity;
import com.example.AccountService.models.Account;
import com.example.AccountService.services.api.IAccountService;
import com.example.AccountService.services.api.MessageError;
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
public class AccountService implements IAccountService {


    private final IAccountStorage accountStorage;
    private final ConversionService conversionService;
    private LocalDateTime localDateTime = LocalDateTime.now();


    public AccountService( IAccountStorage accountStorage, ConversionService conversionService) {

        this.accountStorage = accountStorage;
        this.conversionService = conversionService;

    }


    @Override
    public Account create(Account accountRaw) {

        //   // Проверяем, что обязательные поля не null
        //    if (accountRaw.getType() == null || accountRaw.getTitle() == null || accountRaw.getDescription() == null || accountRaw.getCurrency() == null) {
        //        throw new ValidationException("Пустая строка");
        //    }
        //    // Проверка уникальности title
        //    if (accountStorage.existsByTitle(accountRaw.getTitle())) {
        //        throw new ValidationException("Такой title уже существует ");
        //    }


        check(accountRaw);
        try {
            //создает DtCreate, DtUpdate, Uuid
            accountRaw.setDtCreate(localDateTime);
            accountRaw.setDtUpdate(localDateTime);
            accountRaw.setUuid(UUID.randomUUID());
            accountStorage.save(conversionService.convert(accountRaw, AccountEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return accountRaw;
    }


    @Override
    public PageImpl<Account> getAccounts(int page, int size) {
        // Проверка на положительность значений
        if (page <= 0 ) {
            throw new ValidationException(MessageError.PAGE_NUMBER);
        }
        if ( size <0) {
            throw new ValidationException(MessageError.PAGE_SIZE);
        }
        int start;
        List<Account> accountList;
        int end;
        Pageable pageable;
        try {
            List<AccountEntity> accountEntityList = accountStorage.findAll();
            accountList = new ArrayList<>();
            pageable = Pageable.ofSize(size).withPage(page - 1);
            // Конвертация AccountEntity в Account и добавление в список
            for (int i = 0; i < accountEntityList.size(); i++) {
                AccountEntity accountEntity = accountEntityList.get(i);
                Account account = conversionService.convert(accountEntity, Account.class);
                accountList.add(account);
            }
            //Вычисление индексов start и end для страниц
            start = (int) pageable.getOffset();
            end = Math.min((start + pageable.getPageSize()), accountList.size());
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        // Проверка, что start не выходит за пределы списка
        if (start >= accountList.size()) {
            throw new ValidationException(MessageError.RETRIEVE_ACCOUNTS);
        }
        return new PageImpl<>(accountList.subList(start, end), pageable, accountList.size());
    }


    @Override
    public Account getAccount(UUID uuid) {
        Account account;
        AccountEntity accountEntity;
        try {
            accountEntity = accountStorage.findById(uuid).orElse(null);
            account = conversionService.convert(accountEntity, Account.class);

        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        if (account == null) {
            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
        return account;
    }


    @Override
    public Account updateAccount(UUID uuid, LocalDateTime dtUpdate, Account accountRaw) {

        AccountEntity accountEntity;
        accountEntity = accountStorage.findById(uuid).orElse(null);
        //  // Проверяем, что обязательные поля не null
        //  if (accountRaw.getType() == null || accountRaw.getTitle() == null || accountRaw.getDescription() == null || accountRaw.getCurrency() == null) {
        //      throw new ValidationException("Пустая строка");
        //  }
        //  //Проверка свободен ли такой title
        //  if (accountStorage.existsByTitle(accountRaw.getTitle())) {
        //      throw new ValidationException("Такой title уже существует ");
        //  }



        check(accountRaw);
        //Проверка на наличие счета с этим uuid
        if (accountEntity == null) {
            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
        //Проверка на свежесть данных
        if (!(accountEntity.getDtUpdate().equals(dtUpdate))) {
            throw new ValidationException(MessageError.OUTDATED_DATA);
        }

        try {
            //Обновляет данные Description, DtUpdate, Title, Currency, Title
            accountEntity.setTitle(accountRaw.getTitle());
            accountEntity.setDescription(accountRaw.getDescription());
            accountEntity.setType(String.valueOf(accountRaw.getType()));
            accountEntity.setCurrency(accountRaw.getCurrency());
            accountEntity.setDtUpdate(localDateTime);
            accountStorage.save(accountEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return conversionService.convert(accountEntity, Account.class);
    }


    @Override
    public boolean checkAccount(UUID accountUuid, UUID currency) {
        if (accountStorage.findById(accountUuid).orElse(null).getCurrency().equals(currency)) {
            return true;
        }
        return false;
    }

    @Override
    public void updateBalace(int value, UUID accountUuid) {
        AccountEntity accountEntity = accountStorage.findById(accountUuid).orElse(null);
        accountEntity.setBalance(accountEntity.getBalance() + value);
        accountStorage.save(accountEntity);
    }

    @Override
    public void check(Account accountRaw) {
        // Проверяем, что обязательные поля не null
        if (accountRaw.getType() == null || accountRaw.getTitle() == null || accountRaw.getDescription() == null || accountRaw.getCurrency() == null) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }
        //Проверка свободен ли такой title
        if (accountStorage.existsByTitle(accountRaw.getTitle())) {
            throw new ValidationException(MessageError.TITLE_TAKEN);
        }
    }


}

