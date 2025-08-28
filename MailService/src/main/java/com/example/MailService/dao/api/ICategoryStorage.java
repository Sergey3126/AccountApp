package com.example.MailService.dao.api;

import com.example.MailService.dao.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ICategoryStorage extends JpaRepository<CategoryEntity, UUID> {
    List<CategoryEntity> findByReportUuid(UUID reportUuid);

}
