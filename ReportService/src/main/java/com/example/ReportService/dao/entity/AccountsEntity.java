package com.example.ReportService.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "app")
public class AccountsEntity {
    @Id
    private UUID uuid;
    private UUID account;
    private UUID reportUuid;

    public UUID getReportUuid() {
        return reportUuid;
    }

    public void setReportUuid(UUID reportUuid) {
        this.reportUuid = reportUuid;
    }

    public UUID getAccount() {
        return account;
    }

    public void setAccount(UUID account) {
        this.account = account;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "uuid=" + uuid +
                ", account=" + account +
                ", report=" + reportUuid +
                '}';
    }

    public AccountsEntity() {
    }

    public AccountsEntity(UUID uuid, UUID account, UUID report) {
        this.uuid = uuid;
        this.account = account;
        this.reportUuid = report;
    }
}

