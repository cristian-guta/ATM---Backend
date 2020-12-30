package com.test.demo.repository;

import com.test.demo.model.BenefitAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitAuditRepository extends JpaRepository<BenefitAudit, Integer> {
    Page<BenefitAudit> findAll(Pageable pageable);
}
