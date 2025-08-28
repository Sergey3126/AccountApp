package com.example.ReportService.dao.api;

import com.example.ReportService.dao.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IResultStorage extends JpaRepository<ResultEntity, UUID> {
    ResultEntity findByReportUuid(UUID reportUuid);
}
