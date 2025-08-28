package com.example.ReportService.models;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


public class Operation implements Serializable {
    private UUID uuid;
    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private LocalDateTime date;
    private String description;
    private int value;
    private UUID accountUuid;
    private UUID currency;
    private UUID category;

    public Operation() {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(UUID accountUuid) {
        this.accountUuid = accountUuid;
    }

    public UUID getCurrency() {
        return currency;
    }

    public void setCurrency(UUID currency) {
        this.currency = currency;
    }

    public UUID getCategory() {
        return category;
    }

    public void setCategory(UUID category) {
        this.category = category;
    }

    public Operation(UUID uuid, LocalDateTime dtCreate, LocalDateTime dtUpdate, LocalDateTime date, String description, int value, UUID accountUuid, UUID currency, UUID category) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.date = date;
        this.description = description;
        this.value = value;
        this.accountUuid = accountUuid;
        this.currency = currency;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "uuid=" + uuid +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", accountUuid=" + accountUuid +
                ", currency='" + currency + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
