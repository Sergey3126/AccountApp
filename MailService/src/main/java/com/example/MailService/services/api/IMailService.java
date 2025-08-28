package com.example.MailService.services.api;

import com.example.MailService.models.Params;
import com.example.MailService.models.ReportData;

public interface IMailService {
    /**
     * Создает параметры для отчета
     *
     * @param type      тип отчета
     * @param paramsRaw тело отчета с category(Нужные категории), accounts(Нужные операции), from(с какого числа), to(по какое число), mail(Почта получателя), nick(ник), key(токен)
     * @return сохраненные параметры
     */
    ReportData saveParams(String type, Params paramsRaw);
}
