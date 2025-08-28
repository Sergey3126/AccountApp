package com.example.MailService.dao.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "data", schema = "app")
public class ReportDataEntity {
    @Id
    private UUID uuid;
    private String type;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private UUID reportUuid;
    private String status;
    private String mail;
    private String nick;
    private String key;

    @Override
    public String toString() {
        return "ReportDataEntity{" +
                "uuid=" + uuid +
                ", type='" + type + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", reportUuid=" + reportUuid +
                ", status='" + status + '\'' +
                ", mail='" + mail + '\'' +
                ", nick='" + nick + '\'' +
                ", key='" + key + '\'' +
                '}';
    }


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public ReportDataEntity(UUID uuid, String type, LocalDateTime fromDate, LocalDateTime toDate, UUID reportUuid, String status, String mail, String nick, String key) {
        this.uuid = uuid;
        this.type = type;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reportUuid = reportUuid;
        this.status = status;
        this.mail = mail;
        this.nick = nick;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public UUID getReportUuid() {
        return reportUuid;
    }

    public void setReportUuid(UUID reportUuid) {
        this.reportUuid = reportUuid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public ReportDataEntity() {
    }
}
