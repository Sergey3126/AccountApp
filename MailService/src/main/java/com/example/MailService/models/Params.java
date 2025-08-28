package com.example.MailService.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Params {
    private List<UUID> accounts;
    private List<UUID> category;
    private LocalDateTime from;
    private LocalDateTime to;
    private String mail;
    private String nick;
    private String key;

    @Override
    public String toString() {
        return "Params{" +
                "accounts=" + accounts +
                ", category=" + category +
                ", from=" + from +
                ", to=" + to +
                ", mail='" + mail + '\'' +
                ", nick='" + nick + '\'' +
                ", key='" + key + '\'' +
                '}';
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<UUID> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UUID> accounts) {
        this.accounts = accounts;
    }

    public List<UUID> getCategory() {
        return category;
    }

    public void setCategory(List<UUID> category) {
        this.category = category;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public Params(List<UUID> accounts, List<UUID> category, LocalDateTime from, LocalDateTime to, String mail, String nick, String key) {
        this.accounts = accounts;
        this.category = category;
        this.from = from;
        this.to = to;
        this.mail = mail;
        this.nick = nick;
        this.key = key;
    }

    public Params() {

    }
}
