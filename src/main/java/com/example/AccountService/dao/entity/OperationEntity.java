package com.example.AccountService.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "operation", schema = "app")
public class OperationEntity {
    @Id
    private UUID uuid;
    private UUID accountUuid;
    private LocalDateTime dtCreate;

    private LocalDateTime dtUpdate;
    private LocalDateTime date;
    private String description;
    private int value;
    private UUID currency;
    private UUID category;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(UUID accountUuid) {
        this.accountUuid = accountUuid;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public UUID getCategory() {
        return category;
    }

    public void setCategory(UUID category) {
        this.category = category;
    }

    public UUID getCurrency() {
        return currency;
    }

    public void setCurrency(UUID currency) {
        this.currency = currency;
    }

    public OperationEntity() {
    }

    public OperationEntity(LocalDateTime dtCreate, UUID uuid, UUID accountUuid, LocalDateTime dtUpdate, LocalDateTime date, String description, int value, UUID currency, UUID category) {
        this.dtCreate = dtCreate;
        this.uuid = uuid;
        this.accountUuid = accountUuid;
        this.dtUpdate = dtUpdate;
        this.date = date;
        this.description = description;
        this.value = value;
        this.currency = currency;
        this.category = category;
    }

    @Override
    public String toString() {
        return "OperationEntity{" +
                "uuid=" + uuid +
                ", accountUuid=" + accountUuid +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", currency='" + currency + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
