package com.example.ClassifierService.services.api;


import com.example.ClassifierService.models.OperationCategory;
import org.springframework.data.domain.PageImpl;

import java.util.UUID;

public interface IOperationCategoryService {

    /**
     * Создает категорию трат
     *
     * @param categoryRaw тело категории трат с title(название)
     * @return созданную категорию
     */
    OperationCategory createOperationCategory(OperationCategory categoryRaw);


    /**
     * Дает список категорий по номеру страницы и ее размеру
     *
     * @param page номер страницы (больше 0)
     * @param size кол-во объектов на странице(размер страницы, больше 0)
     * @return список категорий
     */
    PageImpl<OperationCategory> getOperationCategories(int page, int size);

    /**
     * Дает категорию по ключу
     *
     * @param uuid ключ категории
     * @return категорию
     */
    OperationCategory getOperationCategory(UUID uuid);

    /**
     * Дает название категории
     *
     * @param uuid ключ категории
     * @return название категории
     */
    String getCategoryTitle(UUID uuid);
}
