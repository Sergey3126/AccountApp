package com.example.ClassifierService.services;

import com.example.ClassifierService.dao.api.IOperationCategoryStorage;
import com.example.ClassifierService.dao.entity.OperationCategoryEntity;
import com.example.ClassifierService.models.OperationCategory;
import com.example.ClassifierService.services.api.IOperationCategoryService;
import com.example.ClassifierService.services.api.MessageError;
import com.example.ClassifierService.services.api.ValidationException;
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
public class OperationCategoryService implements IOperationCategoryService {
    private final IOperationCategoryStorage operationCategoryStorage;
    private final ConversionService conversionService;
    private LocalDateTime localDateTime = LocalDateTime.now();

    public OperationCategoryService(IOperationCategoryStorage operationCategoryStorage, ConversionService conversionService) {
        this.operationCategoryStorage = operationCategoryStorage;
        this.conversionService = conversionService;
    }


    @Override
    public OperationCategory createOperationCategory(OperationCategory categoryRaw) {

        // Проверяем, что обязательные поля не пусты
        if (categoryRaw.getTitle() == null) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }
        //Проверка свободен ли такой title
        if (operationCategoryStorage.existsByTitle(categoryRaw.getTitle())) {
            throw new ValidationException(MessageError.TITLE_TAKEN);
        }
        try {
            //создает DtCreate, DtUpdate, Uuid
            categoryRaw.setDtCreate(localDateTime);
            categoryRaw.setDtUpdate(localDateTime);
            categoryRaw.setUuid(UUID.randomUUID());
            operationCategoryStorage.save(conversionService.convert(categoryRaw, OperationCategoryEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return categoryRaw;
    }


    @Override
    public PageImpl<OperationCategory> getOperationCategories(int page, int size) {
        // Проверка на положительность значений(что больше 0)
        if (page <= 0) {
            throw new ValidationException(MessageError.PAGE_NUMBER);
        }
        if (size <= 0) {
            throw new ValidationException(MessageError.PAGE_SIZE);
        }
        int start;
        List<OperationCategory> categoryList;
        int end;
        Pageable pageable;
        try {
            List<OperationCategoryEntity> operationCategoryEntityList = operationCategoryStorage.findAll();
            categoryList = new ArrayList<>();
            pageable = Pageable.ofSize(size).withPage(page - 1);
            // Конвертация OperationCategoryEntity в OperationCategory и добавление в список
            for (int i = 0; i < operationCategoryEntityList.size(); i++) {
                OperationCategoryEntity categoryEntity = operationCategoryEntityList.get(i);
                OperationCategory category = conversionService.convert(categoryEntity, OperationCategory.class);
                categoryList.add(category);
            }
            //Вычисление индексов start и end для страниц
            start = (int) pageable.getOffset();
            end = Math.min((start + pageable.getPageSize()), categoryList.size());
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        // Проверка, что start не выходит за пределы списка
        if (start >= categoryList.size()) {
            throw new ValidationException(MessageError.RETRIEVE_OBJECT);
        }
        return new PageImpl<>(categoryList.subList(start, end), pageable, categoryList.size());
    }

    @Override
    public OperationCategory getOperationCategory(UUID uuid) {
        OperationCategory operationCategory;
        OperationCategoryEntity operationCategoryEntity;
        try {
            //Получение категории
            operationCategoryEntity = operationCategoryStorage.findById(uuid).orElse(null);
            operationCategory = conversionService.convert(operationCategoryEntity, OperationCategory.class);

        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        //Проверка, что такая категория есть
        if (operationCategory == null) {

            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
        return operationCategory;
    }

    @Override
    public String getCategoryTitle(UUID uuid) {
        //получает категорию и возвращает ее название
        OperationCategoryEntity operationCategoryEntity = operationCategoryStorage.findById(uuid).orElse(null);
        OperationCategory operationCategory = conversionService.convert(operationCategoryEntity, OperationCategory.class);
        return operationCategory.getTitle();
    }
}




