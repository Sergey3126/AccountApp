package com.example.MailService.dao.controllers.rest;

import com.example.MailService.models.Params;
import com.example.MailService.models.Report;
import com.example.MailService.models.ReportData;
import com.example.MailService.services.api.IMailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {
    private IMailService mailService;

    public MailController(IMailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Создает параметры для отчета
     *
     * @param type      тип отчета
     * @param paramsRaw тело отчета с category(Нужные категории), accounts(Нужные операции), from(с какого числа), to(по какое число), mail(Почта получателя), nick(ник), key(токен)
     * @return сохраненные параметры
     */
    @PostMapping(value = {"report/{type}", "report/{type}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ReportData saveParams(@PathVariable("type") String type, @RequestBody Params paramsRaw) {
        return mailService.saveParams(type, paramsRaw);
    }
}
