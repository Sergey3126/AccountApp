package com.example.MailService.models;


import com.example.MailService.models.api.Status;
import com.example.MailService.models.api.Type;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReportData {
    private UUID uuid;
    private Type type;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private UUID reportUuid;
    private Status status;
    private String mail;
    private String nick;
    private String key;

    @Override
    public String toString() {
        return "ReportData{" +
                "uuid=" + uuid +
                ", type=" + type +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", reportUuid=" + reportUuid +
                ", status=" + status +
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
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

    public ReportData(UUID uuid, Type type, LocalDateTime fromDate, LocalDateTime toDate, UUID reportUuid, String mail, Status status, String key, String nick) {
        this.uuid = uuid;
        this.type = type;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reportUuid = reportUuid;
        this.mail = mail;
        this.status = status;
        this.key = key;
        this.nick = nick;
    }

    public ReportData() {

    }
}
