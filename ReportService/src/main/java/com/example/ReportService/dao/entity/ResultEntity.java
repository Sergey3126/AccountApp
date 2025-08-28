package com.example.ReportService.dao.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "result", schema = "app")
public class ResultEntity {
    @Id
    private UUID uuid;
    @Column(name = "result", columnDefinition = "bytea") // Важно указать bytea
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

    public ResultEntity(UUID uuid, String result, UUID report) {
        this.uuid = uuid;
        this.result = result;
        this.reportUuid = report;
    }

    public ResultEntity() {
    }
}
