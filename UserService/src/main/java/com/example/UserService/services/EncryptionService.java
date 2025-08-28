package com.example.UserService.services;


import com.example.UserService.services.api.IEncryptionService;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class EncryptionService implements IEncryptionService {

    //шаг шифровки
    @Value("${shift}")
    private int shift;

    @Override
    public String encrypt(String nick) {
        return cipher(nick, shift);
    }

    @Override
    public String decrypt(String nick) {
        // Расшифровка - это шифрование со сдвигом в обратную сторону
        return cipher(nick, 26 - shift);
    }

    @Override
    public boolean check(String nick, String key) {
        return Objects.equals(decrypt(key), nick);
    }

    //шифровка
    private String cipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isUpperCase(character) ? 'A' : 'a';
                result.append((char) (((character - base + shift) % 26) + base));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }
}


