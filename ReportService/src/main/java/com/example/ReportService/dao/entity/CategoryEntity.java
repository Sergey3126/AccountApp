package com.example.ReportService.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "categories", schema = "app")
public class CategoryEntity {
    @Id
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

    public CategoryEntity(UUID uuid, UUID categories, UUID report) {
        this.uuid = uuid;
        this.category = categories;
        this.reportUuid = report;
    }

    public CategoryEntity() {
    }
}
