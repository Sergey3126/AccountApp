package com.example.UserService.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import java.util.UUID;


@Entity
@Table(name = "users", schema = "app")
public class UserEntity {
    @Id
    private UUID uuid;
    private String nick;
    private String password;

    public UserEntity() {
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uuid=" + uuid +
                ", nick='" + nick + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public UserEntity(UUID uuid, String nick, String password) {
        this.uuid = uuid;
        this.nick = nick;
        this.password = password;
    }
}
