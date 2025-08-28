package com.example.UserService.services.api;




public interface IEncryptionService {

    /**
     * Проверяет правильность token
     * @param nick ник
     * @return результат
     */
    String encrypt(String nick);
    /**
     * Ди шифрует
     * @param nick ник
     * @return  Ди шифрованный token
     */
    String decrypt(String nick);
    /**
     * Проверяет правильность token
     * @param nick ник
     * @param key токен
     * @return результат
     */
    boolean check(String nick, String key);
}
