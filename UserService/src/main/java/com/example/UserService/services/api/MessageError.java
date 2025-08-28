package com.example.UserService.services.api;

public final class MessageError {
    private MessageError() {
    }


    public static final String BAD_REQUEST = "Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз ";

    public static final String SERVER_ERROR = "Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратору ";

    public static final String NICK_ERROR = "Nick содержит не доступные символы(только латиница)";

    public static final String EMPTY_LINE = "Пустая строка";

    public static final String NICK_TAKEN = " Такой nick уже существует";

    public static final String PASSWORD_BAD= "Неверный пароль";
}
