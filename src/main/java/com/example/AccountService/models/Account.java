package com.example.AccountService.models;

import com.example.AccountService.models.api.TypeOfAccount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Account {
    private UUID uuid;
    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private String title;
    private String description;
    private int balance;
    private TypeOfAccount type;
    private UUID currency;

    public Account() {
    }

    public Account(UUID uuid, LocalDateTime dtCreate, LocalDateTime dtUpdate, String title, String description, UUID currency, int balance, TypeOfAccount type) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.title = title;
        this.description = description;
        this.currency = currency;
        this.balance = balance;
        this.type = type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public TypeOfAccount getType() {
        return type;
    }

    public void setType(TypeOfAccount type) {
        this.type = type;
    }

    public UUID getCurrency() {
        return currency;
    }

    public void setCurrency(UUID currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid=" + uuid +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
