package com.example.ClassifierService.controllers.rest;


import com.example.ClassifierService.models.OperationCategory;
import com.example.ClassifierService.services.api.IOperationCategoryService;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/classifier/operation/category")
public class OperationCategoryController {
    private final IOperationCategoryService operationCategoryService;

    public OperationCategoryController(IOperationCategoryService operationCategoryService) {
        this.operationCategoryService = operationCategoryService;
    }


    /**
     * Создает категорию трат
     *
     * @param categoryRaw тело категории трат с title(название)
     * @return созданную категорию трат
     */
    @PostMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationCategory createOperationCategory(@RequestBody OperationCategory categoryRaw) {
        return operationCategoryService.createOperationCategory(categoryRaw);
    }

    /**
     * Дает список категорий по номеру страницы и ее размеру
     *
     * @param page номер страницы(больше 0)
     * @param size кол-во объектов на странице(размер страницы(больше 0))
     * @return список категорий
     */
    @GetMapping(value = {"{page}/{size}", "{page}/{size}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<OperationCategory> getOperationCategories(@PathVariable int page, @PathVariable int size) {
        return operationCategoryService.getOperationCategories(page, size);
    }

    /**
     * Дает категорию по ключу
     *
     * @param uuid ключ категории
     * @return категорию
     */
    @GetMapping(value = {"{uuid}", "{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public OperationCategory getOperationCategory(@PathVariable UUID uuid) {
        return operationCategoryService.getOperationCategory(uuid);
    }

    /**
     * Дает название категории
     *
     * @param uuid ключ категории
     * @return название категории
     */
    @GetMapping(value = {"{uuid}/title", "{uuid}/title/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getCategoryTitle(@PathVariable UUID uuid) {
        return operationCategoryService.getCategoryTitle(uuid);
    }
}
