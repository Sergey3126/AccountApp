package com.example.ReportService.models;


public class User {
    private String nick;
    private String key;

    public User() {
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


    public User(String nick, String key) {
        this.nick = nick;
        this.key = key;
    }

    @Override
    public String toString() {
        return "User{" +
                ", nick='" + nick + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
