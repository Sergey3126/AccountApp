package com.example.ClassifierService.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Currency {
    private UUID uuid;
    private String title;
    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;

    public Currency(UUID uuid, String title, LocalDateTime dtCreate, LocalDateTime dtUpdate) {
        this.uuid = uuid;
        this.title = title;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
    }

    public Currency() {
    }

    @Override
    public String toString() {
        return "Currency{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


}
