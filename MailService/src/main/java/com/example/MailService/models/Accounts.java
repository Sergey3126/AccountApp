package com.example.MailService.models;

import java.util.UUID;

public class Accounts {
    private UUID uuid;
    private UUID account;
    private UUID reportUuid;

    @Override
    public String toString() {
        return "AccountsEntity{" +
                "uuid=" + uuid +
                ", account=" + account +
                ", reportUuid=" + reportUuid +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getAccount() {
        return account;
    }

    public void setAccount(UUID account) {
        this.account = account;
    }

    public UUID getReportUuid() {
        return reportUuid;
    }

    public void setReportUuid(UUID reportUuid) {
        this.reportUuid = reportUuid;
    }

    public Accounts(UUID uuid, UUID account, UUID reportUuid) {
        this.uuid = uuid;
        this.account = account;
        this.reportUuid = reportUuid;
    }

    public Accounts() {
    }
}
