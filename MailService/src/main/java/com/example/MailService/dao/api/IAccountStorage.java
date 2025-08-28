package com.example.MailService.dao.api;

import com.example.MailService.dao.entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IAccountStorage extends JpaRepository<AccountsEntity, UUID> {
    List<AccountsEntity> findByReportUuid(UUID reportUuid);

}
