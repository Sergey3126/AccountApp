package com.example.MailService.dao.entity;

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

    @Override
    public String toString() {
        return "CategoryEntity{" +
                "uuid=" + uuid +
                ", category=" + category +
                ", reportUuid=" + reportUuid +
                '}';
    }

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

    public CategoryEntity(UUID uuid, UUID category, UUID reportUuid) {
        this.uuid = uuid;
        this.category = category;
        this.reportUuid = reportUuid;
    }

    public CategoryEntity() {
    }
}
