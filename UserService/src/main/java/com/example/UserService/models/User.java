package com.example.UserService.models;


import java.util.UUID;

public class User {
    private UUID uuid;
    private String nick;
    private String password;
    private String key;

    public User() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(UUID uuid, String nick, String password, String key) {
        this.uuid = uuid;
        this.nick = nick;
        this.password = password;
        this.key = key;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", nick='" + nick + '\'' +
                ", password='" + password + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
