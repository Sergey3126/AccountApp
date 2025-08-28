package com.example.ReportService.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Params {
    private List<UUID> accounts;
    private List<UUID> category;
    private LocalDateTime from;
    private LocalDateTime to;
    private String nick;
    private String key;


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

    @Override
    public String toString() {
        return "Params{" +
                "accounts=" + accounts +
                ", category=" + category +
                ", from=" + from +
                ", to=" + to +
                ", nick='" + nick + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public Params(List<UUID> category, List<UUID> accounts, LocalDateTime to, LocalDateTime from, String key, String nick) {
        this.category = category;
        this.accounts = accounts;
        this.to = to;
        this.from = from;
        this.key = key;
        this.nick = nick;
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

    public Params() {
    }
}
