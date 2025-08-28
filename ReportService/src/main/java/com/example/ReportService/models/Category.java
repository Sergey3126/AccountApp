package com.example.ReportService.models;

import java.util.UUID;

public class Category {
    private UUID uuid;
    private UUID category;
    private UUID reportUuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getCategory() {
        return category;
    }

    public void setCategory(UUID category) {
        this.category = category;
    }

    public UUID getReportUuid() {
        return reportUuid;
    }

    public void setReportUuid(UUID reportUuid) {
        this.reportUuid = reportUuid;
    }

    public Category(UUID uuid, UUID categories, UUID report) {
        this.uuid = uuid;
        this.category = categories;
        this.reportUuid = report;
    }

    public Category() {
    }
}
