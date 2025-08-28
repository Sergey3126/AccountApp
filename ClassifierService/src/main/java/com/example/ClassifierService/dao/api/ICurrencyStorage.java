package com.example.ClassifierService.dao.api;

import com.example.ClassifierService.dao.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICurrencyStorage extends JpaRepository<CurrencyEntity, UUID> {
    boolean existsByTitle(String title);
}
