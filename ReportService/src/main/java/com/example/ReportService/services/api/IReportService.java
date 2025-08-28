package com.example.ReportService.services.api;

import com.example.ReportService.models.Params;
import com.example.ReportService.models.Report;
import com.example.ReportService.models.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IReportService {
    /**
     * Создает отчет по параметрам
     *
     * @param type      тип отчета
     * @param paramsRaw тело отчета с category(Нужные категории), accounts(Нужные операции), from(с какого числа), to(по какое число), nick(ник), key(токен)
     * @return созданный отчет
     */
    Report createReport(String type, Params paramsRaw);

    /**
     * Дает отчеты
     *
     * @param page номер страницы(больше 0)
     * @param size кол-во объектов на странице(размер страницы, больше 0)
     * @param user тело авторизации с nick(ник) и key(токен)
     * @return список операций
     */

    PageImpl<Report> getReport(int page, int size, User user);

    /**
     * Выгружает результат в виде excel
     *
     * @param uuid ключ счета
     *  @param user тело авторизации с nick(ник) и key(токен)
     * @return excel файл
     */


    ResponseEntity<ByteArrayResource> exportReport(UUID uuid, User user);

    /**
     * Дает статус отчета
     * @param uuid ключ счета
     * @param user тело авторизации с nick(ник) и key(токен)
     * @return статус
     */


    ResponseEntity<Void> statusReport(UUID uuid, User user);
}
