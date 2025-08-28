package com.example.UserService.dao.api;


import com.example.UserService.dao.entity.UserEntity;
import com.example.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserStorage extends JpaRepository<UserEntity, UUID> {
    UserEntity findByNick(String nick);

    boolean existsByNick(String nick);
}
