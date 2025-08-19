package com.example.AccountService.services.api;

public final class MessageError {
    private MessageError() {
    }


    public static final String PAGE_SIZE = "Размер страницы не может быть меньше 1";

    public static final String PAGE_NUMBER = "Номер страницы не может быть меньше 1";

    public static final String BAD_REQUEST = "Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз ";

    public static final String INCORRECT_TOKEN = "Неверный token";

    public static final String SERVER_ERROR = "Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратору ";

    public static final String RETRIEVE_ACCOUNTS = "Количество аккаунтов меньше запроса";

    public static final String INCORRECT_UUID = "Неверный uuid";

    public static final String OUTDATED_DATA = "Устаревшие данные";

    public static final String EMPTY_LINE = "Пустая строка";

    public static final String TITLE_TAKEN = " Такой title уже существует";

    public static final String INCORRECT_OPERATION = "Cчет не соответствует операции";

    public static final String INCORRECT_CURRENCY = "Currency счета и операции не совпадают ";

    public static final String UUID_CURRENCY = "Переданная Currency отсутствует в списке доступных";

}
