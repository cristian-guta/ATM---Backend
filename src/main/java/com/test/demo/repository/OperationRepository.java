package com.test.demo.repository;

import com.test.demo.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
    Operation findOperationById(int id);

    @Query("select o from Operation o where o.client.id = ?1")
    List<Operation> getOperationsByClientId(int id);
}
