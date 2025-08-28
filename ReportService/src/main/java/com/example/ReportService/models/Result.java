package com.example.ReportService.models;

import java.util.UUID;

public class Result {

    private UUID uuid;
    private String result;
    private UUID reportUuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public UUID getReportUuid() {
        return reportUuid;
    }

    public void setReportUuid(UUID reportUuid) {
        this.reportUuid = reportUuid;
    }

    @Override
    public String toString() {
        return "Result{" +
                "uuid=" + uuid +
                ", result='" + result + '\'' +
                ", report=" + reportUuid +
                '}';
    }

    public Result(UUID uuid, String result, UUID report) {
        this.uuid = uuid;
        this.result = result;
        this.reportUuid = report;
    }

    public Result() {
    }
}

