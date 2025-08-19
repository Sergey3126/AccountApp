package com.example.AccountService.dao.api;


import com.example.AccountService.dao.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAccountStorage extends JpaRepository<AccountEntity, UUID> {
    boolean existsByTitle(String title);

    List<AccountEntity> findByNick(String nick);
}
