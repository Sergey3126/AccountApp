package com.example.ReportService.models;

import java.util.UUID;

public class Accounts {
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

    public Accounts() {
    }

    public Accounts(UUID uuid, UUID account, UUID report) {
        this.uuid = uuid;
        this.account = account;
        this.reportUuid = report;
    }
}
