package com.example.ClassifierService.dao.api;


import com.example.ClassifierService.dao.entity.OperationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOperationCategoryStorage extends JpaRepository<OperationCategoryEntity, UUID> {
    boolean existsByTitle(String title);
}
