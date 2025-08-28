package com.example.ReportService.services;

import com.example.ReportService.dao.api.IAccountStorage;
import com.example.ReportService.dao.api.ICategoryStorage;
import com.example.ReportService.dao.api.IReportStorage;
import com.example.ReportService.dao.api.IResultStorage;
import com.example.ReportService.dao.entity.AccountsEntity;
import com.example.ReportService.dao.entity.CategoryEntity;
import com.example.ReportService.dao.entity.ReportEntity;
import com.example.ReportService.dao.entity.ResultEntity;
import com.example.ReportService.models.*;
import com.example.ReportService.models.api.Status;
import com.example.ReportService.models.api.Type;
import com.example.ReportService.services.api.IReportService;
import com.example.ReportService.services.api.MessageError;
import com.example.ReportService.services.api.ValidationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReportService implements IReportService {

    private final IResultStorage resultStorage;
    private final IReportStorage reportStorage;
    private final IAccountStorage accountStorage;
    private final ICategoryStorage categoryStorage;
    private final ConversionService conversionService;
    private LocalDateTime localDateTime = LocalDateTime.now();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private RestTemplate restTemplate = new RestTemplate();


    public ReportService(IResultStorage resultStorage, IReportStorage reportStorage, IAccountStorage accountStorage, ICategoryStorage categoryStorage, ConversionService conversionService) {
        this.resultStorage = resultStorage;
        this.reportStorage = reportStorage;
        this.accountStorage = accountStorage;
        this.categoryStorage = categoryStorage;
        this.conversionService = conversionService;
    }

    //ссылка для доступа к шифровке
    @Value("${encryption_url}")
    private String encryptionUrl;
    //ссылка для доступа к категории
    @Value("${operation_category_url}")
    private String categoryUrl;
    //ссылка для доступа к счету
    @Value("${account_url}")
    private String accountUrl;
    //ссылка для доступа к списку операций
    @Value("${operation_url}")
    private String operationUrl;
    //ссылка для доступа валюте
    @Value("${currency_url}")
    private String currencyUrl;

    @Override
    public Report createReport(String type, Params paramsRaw) {
        Report reportRaw = new Report();
        Accounts account = new Accounts();

        User user = new User();
        user.setKey(paramsRaw.getKey());
        user.setNick(paramsRaw.getNick());
        checkKey(user);

        // Проверяем, что обязательное поле не пусто
        if (paramsRaw.getAccounts() == null) {
            throw new ValidationException(MessageError.EMPTY_LINE);
        }

        try {
            //создает DtCreate, DtUpdate, Uuid, AccountUuid и сохраняет Status и Type
            reportRaw.setUuid(UUID.randomUUID());
            reportRaw.setDtCreate(localDateTime);
            reportRaw.setDtUpdate(localDateTime);
            reportRaw.setStatus(Status.LOADED);
            reportRaw.setType(Type.valueOf(type));
reportRaw.setKey(user.getKey());
reportRaw.setNick(user.getNick());
            checkAccessibility(accountUrl, paramsRaw.getAccounts(),user.getNick(),user.getKey());

            //сохраняет переданные счета
            for (int i = 0; i < paramsRaw.getAccounts().size(); i++) {
                //создает ReportUuid, Account, Uuid и сохраняет
                account.setUuid(UUID.randomUUID());
                account.setReportUuid(reportRaw.getUuid());
                account.setAccount(paramsRaw.getAccounts().get(i));
                accountStorage.save(conversionService.convert(account, AccountsEntity.class));
            }
            //Поиск по типу отчета
            switch (Type.valueOf(type)) {
                case BALANCE:
                    //сохраняет  Description
                    reportRaw.setDescription("Отчет по " + paramsRaw.getAccounts().size() + " счетам");
                    break;
                case BY_DATE:
                    // Проверяем, что обязательное поле не пусто
                    if (paramsRaw.getTo() == null || paramsRaw.getFrom() == null) {
                        throw new ValidationException(MessageError.EMPTY_LINE);
                    }
                    //сохраняет  Description, FromDate, ToDate
                    reportRaw.setFromDate(paramsRaw.getFrom());
                    reportRaw.setToDate(paramsRaw.getTo());
                    reportRaw.setDescription("Отчет с " + reportRaw.getFromDate().format(formatter) + " по " + reportRaw.getToDate().format(formatter) + " и по " + paramsRaw.getAccounts().size() + " счетам");
                    break;
                case BY_CATEGORY:
                    // Проверяем, что обязательное поле не пусто
                    if (paramsRaw.getTo() == null || paramsRaw.getFrom() == null || paramsRaw.getCategory() == null) {
                        throw new ValidationException(MessageError.EMPTY_LINE);
                    }
                    Category category = new Category();

                    checkAccessibility(categoryUrl, paramsRaw.getCategory(),null,null);

                    //создает ReportUuid, Category, Uuid и сохраняет
                    for (int i = 0; i < paramsRaw.getCategory().size(); i++) {
                        category.setUuid(UUID.randomUUID());
                        category.setReportUuid(reportRaw.getUuid());
                        category.setCategory(paramsRaw.getCategory().get(i));
                        categoryStorage.save(conversionService.convert(category, CategoryEntity.class));
                    }
                    //сохраняет  Description, FromDate, ToDate
                    reportRaw.setFromDate(paramsRaw.getFrom());
                    reportRaw.setToDate(paramsRaw.getTo());
                    reportRaw.setDescription("Отчет с " + reportRaw.getFromDate().format(formatter) + " по " + reportRaw.getToDate().format(formatter) + " и по " + paramsRaw.getAccounts().size() + " счетам и " + paramsRaw.getCategory().size() + " категориям");
                    break;
            }
            reportStorage.save(conversionService.convert(reportRaw, ReportEntity.class));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return reportRaw;
    }

    @Override
    public PageImpl<Report> getReport(int page, int size, User user) {

        checkKey(user);
        // Проверка на положительность значений(что больше 0 и не равен 0)
        if (page <= 0) {
            throw new ValidationException(MessageError.PAGE_NUMBER);
        }
        if (size <= 0) {
            throw new ValidationException(MessageError.PAGE_SIZE);
        }
        int start;
        List<Report> reportList = new ArrayList<>();
        int end;
        Pageable pageable;
        try {
            List<ReportEntity> reportEntityList = reportStorage.findByNick(user.getNick());
            pageable = Pageable.ofSize(size).withPage(page - 1);
            // Конвертация ReportEntity в Report и добавление в список
            for (int i = 0; i < reportEntityList.size(); i++) {
                ReportEntity reportEntity = reportEntityList.get(i);
                Report report = conversionService.convert(reportEntity, Report.class);
                reportList.add(report);
            }
            //Вычисление индексов start и end для страниц
            start = (int) pageable.getOffset();
            end = Math.min((start + pageable.getPageSize()), reportList.size());

        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        // Проверка, что start не выходит за пределы списка
        if (start >= reportList.size()) {
            throw new ValidationException(MessageError.RETRIEVE_REPORT);
        }
        return new PageImpl<>(reportList.subList(start, end), pageable, reportList.size());
    }


    @Override
    public ResponseEntity<ByteArrayResource> exportReport(UUID uuid, User user) {
        checkKey(user);

        //поиск нужного отчета
        ReportEntity reportEntity = new ReportEntity();
        List<ReportEntity> operationEntityList = reportStorage.findByNick(user.getNick());
        for (int i = 0; i < operationEntityList.size(); i++) {
            reportEntity = operationEntityList.get(i);
            if (reportEntity.getUuid() == uuid) {
                break;
            }
        }
        Report report = conversionService.convert(reportEntity, Report.class);
        Map<UUID, Integer> balance = new HashMap<>();
        Map<UUID, List<Operation>> operationMap = new HashMap<>();
        List<Operation> operationList = new ArrayList<>();

        //проверка, что есть такой отчет и он готов
        if (report == null) {
            throw new ValidationException(MessageError.UUID_REPORT);
        }
        if (report.getStatus() != Status.DONE) {
            throw new ValidationException(MessageError.REPORT_STATUS + report.getStatus());
        }
        Result result = conversionService.convert(resultStorage.findByReportUuid(uuid), Result.class);
        int numCols = 0;
        Row row = null;
        int sum;
        int total = 0;
        String currency;
        int rowInt = 3;
        String title = "";
        String exportDirectory = "";
        String type = "";

        try {
            switch (report.getType()) {
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
            //создание файла и страницы excel
            String filename = String.format("Отчет %s %s.xlsx", type, report.getDtCreate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
            Path directoryPath = Paths.get(exportDirectory);
            Files.createDirectories(directoryPath);
            Path filePath = directoryPath.resolve(filename);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Отчет " + type);


            Row headerRow = sheet.createRow(1);

            //получает результата отчета и счетов
            byte[] serializedData = Base64.getDecoder().decode(result.getResult());
            ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedData);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            List<AccountsEntity> accountEntityList = accountStorage.findByReportUuid(uuid);

            // Стиль для толстой внешней границы
            CellStyle thickBorderStyle = workbook.createCellStyle();
            thickBorderStyle.setBorderBottom(BorderStyle.THICK);
            thickBorderStyle.setBorderTop(BorderStyle.THICK);
            thickBorderStyle.setBorderLeft(BorderStyle.THICK);
            thickBorderStyle.setBorderRight(BorderStyle.THICK);

            // Стиль для толстой внешней границы и центра
            CellStyle thickBorderStyleCenter = workbook.createCellStyle();
            thickBorderStyleCenter.cloneStyleFrom(thickBorderStyle);
            thickBorderStyleCenter.setAlignment(HorizontalAlignment.CENTER);
            thickBorderStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);

            // Стиль для тонкой внутренней границы
            CellStyle thinBorderStyle = workbook.createCellStyle();
            thinBorderStyle.cloneStyleFrom(thickBorderStyle);
            thinBorderStyle.setBorderBottom(BorderStyle.THIN);
            thinBorderStyle.setBorderTop(BorderStyle.THIN);

            //Стиль для нижней границы
            CellStyle bottomStyle = workbook.createCellStyle();
            bottomStyle.cloneStyleFrom(thinBorderStyle);
            bottomStyle.setBorderBottom(BorderStyle.THICK);

            //Стиль для верхней границы
            CellStyle topStyle = workbook.createCellStyle();
            topStyle.cloneStyleFrom(thinBorderStyle);
            topStyle.setBorderTop(BorderStyle.THICK);


            switch (report.getType()) {
                case BALANCE:
                    balance = (Map<UUID, Integer>) objectStream.readObject();
                    objectStream.close();

                    //создание оформления
                    createAndSetCell(workbook, headerRow, 0, "Счета", IndexedColors.WHITE, thickBorderStyleCenter);
                    createAndSetCell(workbook, headerRow, 1, "Тип счета", IndexedColors.WHITE, thickBorderStyleCenter);
                    createAndSetCell(workbook, headerRow, 2, "Сумма", IndexedColors.WHITE, thickBorderStyleCenter);
                    numCols = headerRow.getLastCellNum();
                    createTitle(sheet, numCols, workbook, "Балансы счетов", thickBorderStyleCenter);
                    //создание строк с названием счета, балансом, и типом счета
                    for (int i = 0; i < accountEntityList.size(); i++) {
                        UUID uuidAccount = accountEntityList.get(i).getAccount();
                        Account account = accountObject(uuidAccount,report.getNick(),report.getKey());
                        currency = title(account.getCurrency(), currencyUrl);
                        sum = balance.get(uuidAccount);

                        row = sheet.createRow(rowInt - 1);
                        rowInt++;

                        createAndSetCell(workbook, row, 0, account.getTitle(), IndexedColors.WHITE, thinBorderStyle);
                        //получение типа счета
                        switch (account.getType()) {
                            case CASH:
                                type = "Наличные деньги";
                                break;
                            case BANK_ACCOUNT:
                                type = "Счёт в банке";
                                break;
                            case BANK_DEPOSIT:
                                type = "Депозит в банке";
                                break;
                        }
                        createAndSetCell(workbook, row, 1, type, IndexedColors.WHITE, thinBorderStyle);

                        if (sum > 0) {
                            createAndSetCell(workbook, row, 2, sum + " " + currency, IndexedColors.GREEN, thinBorderStyle);
                        } else if (sum < 0) {
                            createAndSetCell(workbook, row, 2, sum + " " + currency, IndexedColors.RED, thinBorderStyle);
                        } else {
                            createAndSetCell(workbook, row, 2, sum + " " + currency, IndexedColors.WHITE, thinBorderStyle);
                        }
                    }
                    saveBorder(row, numCols, workbook, "Bottom");
                    for (int k = 0; k < numCols - 1; k++) {
                        row.getCell(k).setCellStyle(bottomStyle);
                    }
                    break;
                case BY_DATE:
                case BY_CATEGORY:
                    operationMap = (Map<UUID, List<Operation>>) objectStream.readObject();
                    objectStream.close();


                    if (report.getType() == Type.BY_DATE) {
                        title = "Операции счетов относительно даты";
                    } else if (report.getType() == Type.BY_CATEGORY) {
                        title = "Операции счетов относительно даты и категории трат";
                    }


                    //создание оформления

                    createAndSetCell(workbook, headerRow, 0, "Счета", IndexedColors.WHITE, thickBorderStyleCenter);
                    createAndSetCell(workbook, headerRow, 1, "Тип счета", IndexedColors.WHITE, thickBorderStyleCenter);
                    createAndSetCell(workbook, headerRow, 2, "Описание операции", IndexedColors.WHITE, thickBorderStyleCenter);
                    createAndSetCell(workbook, headerRow, 3, "Категория трат", IndexedColors.WHITE, thickBorderStyleCenter);
                    createAndSetCell(workbook, headerRow, 4, "Дата операции", IndexedColors.WHITE, thickBorderStyleCenter);
                    createAndSetCell(workbook, headerRow, 5, "Сумма", IndexedColors.WHITE, thickBorderStyleCenter);
                    numCols = headerRow.getLastCellNum();
                    createTitle(sheet, numCols, workbook, title, thickBorderStyleCenter);
                    row = sheet.createRow(rowInt);
//заполнение данными имя счета, тип счета, описание операции, категория трат, дата операции, сумма операции
                    for (int i = 0; i < accountEntityList.size(); i++) {
                        int lastRowNum;
                        int firstRowNum;
                        UUID uuidAccount = accountEntityList.get(i).getAccount();
                        Account account = accountObject(uuidAccount,report.getNick(),report.getKey());
                        operationList = operationMap.get(uuidAccount);
                        currency = title(account.getCurrency(), currencyUrl);

                        createAndSetCell(workbook, row, 0, account.getTitle(), IndexedColors.WHITE, thickBorderStyle);
                        //получение типа счета
                        switch (account.getType()) {
                            case CASH:
                                type = "Наличные деньги";
                                break;
                            case BANK_ACCOUNT:
                                type = "Счёт в банке";
                                break;
                            case BANK_DEPOSIT:
                                type = "Депозит в банке";
                                break;
                        }
                        createAndSetCell(workbook, row, 1, type, IndexedColors.WHITE, thickBorderStyle);
                        firstRowNum = rowInt;
                        if (operationList.isEmpty()) {
                            for (int j = 2; j < 6; j++) {
                                createAndSetCell(workbook, row, j, "Пусто", IndexedColors.BLUE, thickBorderStyleCenter);
                            }
                            rowInt++;
                            row = sheet.createRow(rowInt);
                        }
                        for (int j = 0; j < operationList.size(); j++) {
                            rowInt++;
                            Operation operation = operationList.get(j);
                            sum = operation.getValue();
                            if (sum > 0) {
                                createAndSetCell(workbook, row, 5, sum + " " + currency, IndexedColors.GREEN, thinBorderStyle);
                            } else if (sum < 0) {
                                createAndSetCell(workbook, row, 5, sum + " " + currency, IndexedColors.RED, thinBorderStyle);
                            } else {
                                createAndSetCell(workbook, row, 5, sum + " " + currency, IndexedColors.WHITE, thinBorderStyle);
                            }

                            createAndSetCell(workbook, row, 2, operation.getDescription(), IndexedColors.WHITE, thinBorderStyle);

                            createAndSetCell(workbook, row, 3, title(operation.getCategory(), categoryUrl), IndexedColors.WHITE, thinBorderStyle);

                            createAndSetCell(workbook, row, 4, operation.getDate(), IndexedColors.WHITE, thinBorderStyle);

                            total = total + sum;
                            row = sheet.createRow(rowInt);
                        }
                        lastRowNum = rowInt - 1;

                        if (total > 0) {
                            createAndSetCell(workbook, row, 5, "Итого: " + total + " " + currency, IndexedColors.GREEN, thinBorderStyle);
                        } else if (total < 0) {
                            createAndSetCell(workbook, row, 5, "Итого: " + total + " " + currency, IndexedColors.RED, thinBorderStyle);
                        } else {
                            createAndSetCell(workbook, row, 5, "Итого: " + total + " " + currency, IndexedColors.WHITE, thinBorderStyle);
                        }

                        saveBorder(row, numCols, workbook, "Bottom");


                        total = 0;
                        rowInt = rowInt + 2;
                        row = sheet.createRow(rowInt);


                        Row lastRow = sheet.getRow(lastRowNum);
                        Row firstRow = sheet.getRow(firstRowNum);

                        saveBorder(firstRow, numCols, workbook, "Top");


                        //создание верхних и нижних границ
                        if (!lastRow.getCell(3).getStringCellValue().toString().equals("Пусто") && lastRow != firstRow) {
                            for (int k = 2; k < numCols - 1; k++) {
                                lastRow.getCell(k).setCellStyle(bottomStyle);
                                firstRow.getCell(k).setCellStyle(topStyle);
                            }
                        } else if (lastRow == firstRow && !lastRow.getCell(3).getStringCellValue().equals("Пусто")) {
                            for (int k = 2; k < numCols - 1; k++) {
                                lastRow.getCell(k).setCellStyle(thickBorderStyle);
                            }
                        }
                    }


                    break;
            }

            //авто размер столбца
            for (int i = 0; i < numCols; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            out.close();

            // возвращаем файл и скачиваем
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename);
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);


        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<Void> statusReport(UUID uuid, User user) {
        checkKey(user);
        //поиск нужного отчета
        ReportEntity reportEntity = new ReportEntity();
        List<ReportEntity> operationEntityList = reportStorage.findByNick(user.getNick());
        for (int i = 0; i < operationEntityList.size(); i++) {
            reportEntity = operationEntityList.get(i);
            if (reportEntity.getUuid() == uuid) {
                break;
            }
        }
        if (reportEntity == null) {
            throw new ValidationException(MessageError.UUID_REPORT);
        }
        try {
            if (reportEntity == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);// 404 не найден
            }
            switch (Status.valueOf(reportEntity.getStatus())) {
                case DONE:
                    return new ResponseEntity<>(HttpStatus.OK); // 200 все хорошо
                case LOADED:
                case PROGRESS:
                    return new ResponseEntity<>(HttpStatus.ACCEPTED); // 202 в процессе
                case ERROR:
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Ошибка
            }
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(MessageError.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Scheduled(fixedRate = 10000)
    private void reportProcessing() {
        Report report = new Report();
        Map<UUID, Integer> balance = new HashMap<>();
        Result result = new Result();
        Map<UUID, List<Operation>> mapOperation = new HashMap<>();
        try {
//получает все необработанные отчеты
            List<ReportEntity> reportEntityList = reportStorage.findByStatus(Status.LOADED.toString());

//обрабатывает отчеты по одному
            for (int i = 0; i < reportEntityList.size(); i++) {
                //Получает отчет и требуемые счета, изменяет статус на PROGRESS(В процессе)
                ReportEntity reportEntity = reportEntityList.get(i);
                report = conversionService.convert(reportEntity, Report.class);
                report.setStatus(Status.PROGRESS);
                reportStorage.save(conversionService.convert(report, ReportEntity.class));
                List<AccountsEntity> accountList = accountStorage.findByReportUuid(report.getUuid());

                //обрабатывает отчет относительно типа
                switch (report.getType()) {
                    case BALANCE:
                        //получает счет и сохраняет его баланс
                        for (int j = 0; j < accountList.size(); j++) {
                            UUID uuid = accountList.get(j).getAccount();
                            Account account = accountObject(uuid,report.getNick(),report.getKey());
                            balance.put(uuid, account.getBalance());
                        }

                        break;
                    case BY_DATE:
                        //Сортировка по дате
                        mapOperation = sortOperationByDate(report, accountList);
                        break;
                    case BY_CATEGORY:
                        //Сортировка по дате и получение требуемых категорий
                        mapOperation = sortOperationByDate(report, accountList);
                        List<CategoryEntity> categoryList = categoryStorage.findByReportUuid(report.getUuid());

                        for (int j = 0; j < accountList.size(); j++) {
                            //получение сортированных операций и сортировка относительно категории
                            List<Operation> operationList = mapOperation.get(accountList.get(j).getAccount());
                            for (int e = operationList.size() - 1; e >= 0; e--) {
                                boolean check = false;
                                Operation operation = operationList.get(e);
                                //проверка на совпадение категории операции с доступными
                                for (int o = 0; o < categoryList.size(); o++) {
                                    if (Objects.equals(String.valueOf(categoryList.get(o).getCategory()), String.valueOf(operation.getCategory()))) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (!check) {
                                    operationList.remove(e);
                                }

                            }
                            mapOperation.put(accountList.get(j).getAccount(), operationList);
                        }
                        break;
                }
                //сохранение результата относительно типа отчета и изменение статуса отчета DONE(Сделан)
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
                if (report.getType() == Type.BALANCE) {
                    objectStream.writeObject(balance);
                } else if (report.getType() == Type.BY_CATEGORY || report.getType() == Type.BY_DATE) {
                    objectStream.writeObject(mapOperation);
                }
                objectStream.close();
                byte[] serializedData = byteStream.toByteArray();
                String baseResult = Base64.getEncoder().encodeToString(serializedData);
                result.setUuid(UUID.randomUUID());
                result.setResult(baseResult);
                result.setReportUuid(report.getUuid());
                resultStorage.save(conversionService.convert(result, ResultEntity.class));
                report.setStatus(Status.DONE);
                reportStorage.save(conversionService.convert(report, ReportEntity.class));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            report.setStatus(Status.ERROR);
            reportStorage.save(conversionService.convert(report, ReportEntity.class));
        }
    }

    //Создание главной/титульной ячейки
    private void createTitle(Sheet sheet, int lastCol, Workbook workbook, String title, CellStyle style) {
        CellStyle styleRegion = workbook.createCellStyle();
        styleRegion.cloneStyleFrom(style);
        //Цвет главной ячейки
        styleRegion.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleRegion.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        styleRegion.setFont(boldFont);

        //создаем главную ячейку
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, lastCol - 1);
        sheet.addMergedRegion(region);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(title);

        //Применяем стиль для объединенной ячейки
        CellRangeAddress mergedRegion = sheet.getMergedRegion(0);
        for (int rowNum = mergedRegion.getFirstRow(); rowNum <= mergedRegion.getLastRow(); rowNum++) {
            row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
            }
            for (int colNum = mergedRegion.getFirstColumn(); colNum <= mergedRegion.getLastColumn(); colNum++) {
                cell = row.getCell(colNum);
                if (cell == null) {
                    cell = row.createCell(colNum);
                }
                cell.setCellStyle(styleRegion);
            }
        }
    }

    //получает счет по ключу
    private Account accountObject(UUID uuid,String nick,String key) {
        Account account;
        try (InputStream stream = new URL(accountUrl + uuid+"/"+nick+"/"+key).openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            String accountStr = reader.lines().collect(Collectors.joining("\n"));
            objectMapper.registerModule(new JavaTimeModule());
            account = objectMapper.readValue(accountStr, Account.class);

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.UUID_ACCOUNT);

        }
        return account;
    }

    //получает название
    private String title(UUID uuid, String url) {
        String title;
        try (InputStream stream = new URL(url + uuid + "/title").openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            title = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new ValidationException(MessageError.INCORRECT_UUID);
        }
        return title;
    }

    //сортировка по дате
    private Map<UUID, List<Operation>> sortOperationByDate(Report report, List<AccountsEntity> accountList) {
        Map<UUID, List<Operation>> mapOperation = new HashMap<>();
        List<Operation> operations = new ArrayList<>();

        for (int j = 0; j < accountList.size(); j++) {
            String uuid = String.valueOf(accountList.get(j).getAccount());
            //получает операции счета
            User user = new User();
            user.setKey(report.getKey());
            user.setNick(report.getNick());

try {

    String jsonUser = objectMapper.writeValueAsString(user);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> str = new HttpEntity<>(jsonUser, headers);
    ResponseEntity<List> response = restTemplate.postForEntity(operationUrl + uuid + "/operation", str,List.class     );
operations =  operations = objectMapper.convertValue(response.getBody(), new TypeReference<List<Operation>>() {});
}catch (IOException e) {
    throw new ValidationException(MessageError.UUID_OPERATION);
}

            //сортировка по дате
            for (int e = operations.size() - 1; e >= 0; e--) {
                if (operations.get(e).getDate().isBefore(report.getFromDate()) || operations.get(e).getDate().isAfter(report.getToDate())) {
                    operations.remove(e);
                }
            }

            mapOperation.put(accountList.get(j).getAccount(), operations);

        }

        return mapOperation;

    }

    //создание и заполнение ячейки
    public void createAndSetCell(Workbook workbook, Row row, int columnIndex, Object value, IndexedColors color, CellStyle style) {
        // Создаем новый стиль
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.cloneStyleFrom(style);

        cellStyle.setFillForegroundColor(color.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Cell cell = row.createCell(columnIndex);
        //поиск типа данных
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof LocalDateTime) {
                LocalDateTime dateTime = (LocalDateTime) value;
                cell.setCellValue(dateTime.format(formatter));
            } else {
                cell.setCellValue(value.toString());
            }
        } else {
            cell.setCellValue("Пусто");
        }
        cell.setCellStyle(cellStyle);
    }

    //меняет верхнюю или нижнюю границу крашеной ячейки с сохранением цвета
    private void saveBorder(Row row, int num, Workbook workbook, String border) {
        CellStyle style = row.getCell(num - 1).getCellStyle();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.cloneStyleFrom(style);
        if (border.equals("Bottom")) {
            cellStyle.setBorderBottom(BorderStyle.THICK);
        } else if (border.equals("Top")) {
            cellStyle.setBorderTop(BorderStyle.THICK);
        }
        row.getCell(num - 1).setCellStyle(cellStyle);
    }

    //проверят доступность
    private void checkAccessibility(String url, List<UUID> uuidList,String nick,String key) {
        for (int i = 0; i < uuidList.size(); i++) {
            String uuid = String.valueOf(uuidList.get(i));
            try (InputStream stream = new URL(url + uuid+"/"+nick+"/"+key).openStream()) {
                //получает валюту
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                String currency = reader.lines().collect(Collectors.joining("\n"));

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
