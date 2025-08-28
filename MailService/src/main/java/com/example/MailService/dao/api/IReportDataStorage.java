package com.example.MailService.dao.api;

import com.example.MailService.dao.entity.ReportDataEntity;
import com.example.MailService.models.api.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IReportDataStorage extends JpaRepository<ReportDataEntity, UUID> {
    List<ReportDataEntity> findAll();

    List<ReportDataEntity> findByStatus(String status);
}
