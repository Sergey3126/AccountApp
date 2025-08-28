package com.example.AccountService.dao.api;


import com.example.AccountService.dao.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IOperationStorage extends JpaRepository<OperationEntity, UUID> {


    List<OperationEntity> findByNick(String nick);
}
