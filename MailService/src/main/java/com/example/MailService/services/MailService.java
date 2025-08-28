package com.example.MailService.services;

import com.example.MailService.dao.api.IAccountStorage;
import com.example.MailService.dao.api.ICategoryStorage;
import com.example.MailService.dao.api.IReportDataStorage;

import com.example.MailService.dao.entity.AccountsEntity;
import com.example.MailService.dao.entity.CategoryEntity;
import com.example.MailService.dao.entity.ReportDataEntity;

import com.example.MailService.models.*;
import com.example.MailService.models.api.Status;
import jakarta.activation.DataSource;
import com.example.MailService.models.api.Type;
import com.example.MailService.services.api.IMailService;
import com.example.MailService.services.api.MessageError;
import com.example.MailService.services.api.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


@Service
public class MailService implements IMailService {
    private final IReportDataStorage reportStorage;
    private final IAccountStorage accountStorage;
    private final ICategoryStorage categoryStorage;
    private final ConversionService conversionService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate restTemplate = new RestTemplate();
    private LocalDateTime localDateTime = LocalDateTime.now();


    //почта отправки
    @Value("${service_email}")
    private String email;
    //пароль почты
    @Value("${service_password}")
    private String password;
    //ссылка для доступа к категории
    @Value("${operation_category_url}")
    private String categoryUrl;
    //ссылка для доступа к счету
    @Value("${account_url}")
    private String accountUrl;
    //ссылка для доступа к отчетам
    @Value("${report_url}")
    private String reportUrl;
    //ссылка для доступа к шифровке
    @Value("${encryption_url}")
    private String encryptionUrl;

    public MailService(IReportDataStorage reportStorage, IAccountStorage accountStorage, ICategoryStorage categoryStorage, ConversionService conversionService) {
        this.reportStorage = reportStorage;
        this.accountStorage = accountStorage;
        this.categoryStorage = categoryStorage;
        this.conversionService = conversionService;
    }

    @Override
    public ReportData saveParams(String type, Params paramsRaw) {

        ReportData reportDataRaw = new ReportData();
        Accounts account = new Accounts();
        User user = new User();
        user.setKey(paramsRaw.getKey());
        user.setNick(paramsRaw.getNick());
        checkKey(user);
        // Проверяем, что обязательное поле не пусто
        if (paramsRaw.getAccounts() == null || paramsRaw.getMail() == null) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }

        try {
            //создает Uuid, AccountUuid и сохраняет Status, Key, Nick, Mail и Type
            reportDataRaw.setMail(paramsRaw.getMail());
            reportDataRaw.setUuid(UUID.randomUUID());
            reportDataRaw.setType(Type.valueOf(type));
            reportDataRaw.setStatus(Status.LOADED);
            reportDataRaw.setKey(paramsRaw.getKey());
            reportDataRaw.setNick(paramsRaw.getNick());

            checkAccessibility(accountUrl, paramsRaw.getAccounts(), user);

            //сохраняет переданные счета
            for (int i = 0; i < paramsRaw.getAccounts().size(); i++) {
                //создает ReportUuid, Account, Uuid и сохраняет
                account.setUuid(UUID.randomUUID());
                account.setReportUuid(reportDataRaw.getUuid());
                account.setAccount(paramsRaw.getAccounts().get(i));
                accountStorage.save(conversionService.convert(account, AccountsEntity.class));
            }
            //Сохранение дат
            if (paramsRaw.getTo() == null || paramsRaw.getFrom() == null) {
                //если нет временных рамок или одно поле пустое, то отчет каждый месяц
                reportDataRaw.setFromDate(localDateTime.withDayOfMonth(1));
                reportDataRaw.setToDate(localDateTime.with(TemporalAdjusters.lastDayOfMonth()));
            } else if (Type.valueOf(type) != Type.BALANCE) {
                reportDataRaw.setFromDate(paramsRaw.getFrom());
                reportDataRaw.setToDate(paramsRaw.getTo());
            }
            if (Type.valueOf(type) == Type.BY_CATEGORY) {
                // Проверяем, что обязательное поле не пусто
                if (paramsRaw.getCategory() == null) {
                    throw new ValidationException(MessageError.EMPTY_LINE);
                }
                Category category = new Category();

                checkAccessibility(categoryUrl, paramsRaw.getCategory(), user);

                //создает ReportUuid, Category, Uuid и сохраняет
                for (int i = 0; i < paramsRaw.getCategory().size(); i++) {
                    category.setUuid(UUID.randomUUID());
                    category.setReportUuid(reportDataRaw.getUuid());
                    category.setCategory(paramsRaw.getCategory().get(i));
                    categoryStorage.save(conversionService.convert(category, CategoryEntity.class));
                }
            }
            reportStorage.save(conversionService.convert(reportDataRaw, ReportDataEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return reportDataRaw;

    }

    //Создание отчета
    @Scheduled(fixedRate = 10000)
    private void createReport() {
        ReportData reportData = new ReportData();
        try {
            //Получение листа отчетов
            objectMapper.registerModule(new JavaTimeModule());
            Params paramsRaw = new Params();
            List<ReportDataEntity> reportDataEntityList = reportStorage.findByStatus(Status.LOADED.toString());
            for (int i = 0; i < reportDataEntityList.size(); i++) {
                reportData = conversionService.convert(reportDataEntityList.get(i), ReportData.class);
                Duration difference = Duration.between(reportData.getFromDate(), reportData.getToDate());
                long secondsDifference = difference.getSeconds();
                //Проверка, что отчет не раньше рамок
                if (localDateTime.isAfter(reportData.getToDate())) {

                    //Получение листов категорий и счетов
                    List<AccountsEntity> accountsEntityList = accountStorage.findByReportUuid(reportData.getUuid());
                    List<UUID> accountsList = new ArrayList<>();
                    for (int j = 0; j < accountsEntityList.size(); j++) {
                        Accounts accounts = conversionService.convert(accountsEntityList.get(j), Accounts.class);
                        accountsList.add(accounts.getAccount());
                    }
                    List<CategoryEntity> categoryEntityList = categoryStorage.findByReportUuid(reportData.getUuid());
                    List<UUID> categoryList = new ArrayList<>();
                    for (int j = 0; j < categoryEntityList.size(); j++) {
                        Category category = conversionService.convert(categoryEntityList.get(j), Category.class);
                        categoryList.add(category.getCategory());
                    }
                    //Сохранение и передача счетов, категорий, дат
                    paramsRaw.setCategory(categoryList);
                    paramsRaw.setAccounts(accountsList);
                    paramsRaw.setFrom(reportData.getFromDate());
                    paramsRaw.setTo(reportData.getToDate());
                    paramsRaw.setKey(reportData.getKey());
                    paramsRaw.setNick(reportData.getNick());
                    //создание отчета
                    String jsonParams = objectMapper.writeValueAsString(paramsRaw);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> reportStr = new HttpEntity<>(jsonParams, headers);
                    ResponseEntity<Report> response = restTemplate.postForEntity(reportUrl + "report/" + reportData.getType(), reportStr, Report.class);
                    Report report = response.getBody();
                    reportData.setReportUuid(report.getUuid());
                    reportData.setStatus(Status.PROGRESS);

                    //сохранение дат для следуещего раза
                    reportData.setFromDate(reportData.getFromDate().plusSeconds(secondsDifference));
                    reportData.setToDate(reportData.getToDate().plusSeconds(secondsDifference));
                    reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));

                }
            }
        } catch (IOException e) {
            reportData.setStatus(Status.ERROR);
            reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.BAD_REQUEST);
        }
    }

    //Проверка статуса отчета
    @Scheduled(fixedRate = 10000)
    private void checkStatus() {
        ReportData reportData = new ReportData();
        try {
            List<ReportDataEntity> reportDataEntityList = reportStorage.findByStatus(Status.PROGRESS.toString());
            for (int i = 0; i < reportDataEntityList.size(); i++) {
                reportData = conversionService.convert(reportDataEntityList.get(i), ReportData.class);

                User user = new User();
                user.setKey(reportData.getKey());
                user.setNick(reportData.getNick());
                //получение статуса
                String jsonUser = objectMapper.writeValueAsString(user);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> str = new HttpEntity<>(jsonUser, headers);
                ResponseEntity<Integer> response = restTemplate.postForEntity(reportUrl + "account/" + reportData.getReportUuid() + "/export", str, int.class);
                int statusCode = response.getBody();



                switch (statusCode) {
                    case 200:
                        reportData.setStatus(Status.DONE);
                        reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));
                        break;
                    case 500:
                        reportData.setStatus(Status.ERROR);
                        reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));
                        break;
                }
            }
        } catch (IOException e) {
            reportData.setStatus(Status.ERROR);
            reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
    }

    //Отправка отчета на почту
    @Scheduled(fixedRate = 10000)
    private void sendEmailWithAttachment() {

        List<ReportDataEntity> reportDataEntityList = reportStorage.findByStatus(Status.DONE.toString());
        for (int i = 0; i < reportDataEntityList.size(); i++) {
            ReportData reportData = conversionService.convert(reportDataEntityList.get(i), ReportData.class);
            String to = reportData.getMail();
            String subject = "Готовый отчет";
            String body = "Ваш автоматический отчет.";
            String type = "";
            //создание имени отчета
            switch (reportData.getType()) {
                case BALANCE:
                    type = "балансов";
                    break;
                case BY_DATE:
                    type = "операций по дате";
                    break;
                case BY_CATEGORY:
                    type = "операций по категориям";
                    break;
            }
            String filename = String.format("Отчет %s до %s.xlsx", type, reportData.getToDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            // Настройки почтового сервера
            String host = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.debug", "true");

            // Создаем сессию
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            });

            try {
                //Создаем письмо
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject(subject);
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                MimeBodyPart attachmentPart = new MimeBodyPart();

                User user = new User();
                user.setKey(reportData.getKey());
                user.setNick(reportData.getNick());


                String jsonUser = objectMapper.writeValueAsString(user);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> str = new HttpEntity<>(jsonUser, headers);
                ResponseEntity<byte[]> response = restTemplate.postForEntity(reportUrl + "account/" + reportData.getReportUuid() + "/export", str, byte[].class);
                byte[] excelData = response.getBody();



                DataSource dataSource = new ByteArrayDataSource(excelData, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                attachmentPart.setDataHandler(new DataHandler(dataSource));
                attachmentPart.setFileName(filename);
                //  добавляем текст, вложение и контент сообщения
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);
                message.setContent(multipart);
                // Отправляем письмо
                Transport.send(message);
                reportData.setStatus(Status.LOADED);
                reportData.setReportUuid(null);
                reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));

            } catch (MessagingException e) {
                reportData.setStatus(Status.ERROR);
                reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));
            } catch (IOException e) {
                reportData.setStatus(Status.ERROR);
                reportStorage.save(conversionService.convert(reportData, ReportDataEntity.class));

            }
        }

    }

    //проверят доступность
    private void checkAccessibility(String url, List<UUID> uuidList, User user) {
        for (int i = 0; i < uuidList.size(); i++) {
            try {
                String jsonUser = objectMapper.writeValueAsString(user);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> str = new HttpEntity<>(jsonUser, headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url + "check", str, String.class);
            } catch (IOException e) {

                throw new ValidationException(MessageError.INCORRECT_UUID);

            }
        }
    }

    //проверка авторизации
    private void checkKey(User user) {
        // Проверяем, что обязательные поля не пусты
        if (user.getNick() == null || user.getKey() == null) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }
        try {
            //получаем совпадает ли токен
            String jsonUser = objectMapper.writeValueAsString(user);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> str = new HttpEntity<>(jsonUser, headers);
            ResponseEntity<Boolean> response = restTemplate.postForEntity(encryptionUrl + "check", str, boolean.class);
            boolean bool = response.getBody();

            if (!bool) {
                throw new ValidationException(MessageError.INCORRECT_TOKEN);
            }
        } catch (IOException e) {
            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
    }
}
