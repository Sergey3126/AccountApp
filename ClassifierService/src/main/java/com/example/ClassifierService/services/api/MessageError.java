package com.example.ClassifierService.services.api;

public final class MessageError {
    private MessageError() {
    }


    public static final String PAGE_SIZE = "Размер страницы не может быть меньше 1";

    public static final String PAGE_NUMBER = "Номер страницы не может быть меньше 1";

    public static final String BAD_REQUEST = "Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз ";

    public static final String SERVER_ERROR = "Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратору ";

    public static final String RETRIEVE_OBJECT = "Количество объектов меньше запроса";

    public static final String EMPTY_LINE = "Пустая строка";

    public static final String TITLE_TAKEN = " Такой title уже существует";

    public static final String INCORRECT_UUID = "Неверный uuid";
}
