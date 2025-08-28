package com.example.ReportService.dao.api;

import com.example.ReportService.dao.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IReportStorage extends JpaRepository<ReportEntity, UUID> {


    List<ReportEntity> findByNick(String nick);

    List<ReportEntity> findByStatus(String status);
}
