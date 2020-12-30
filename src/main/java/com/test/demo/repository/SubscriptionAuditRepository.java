package com.test.demo.repository;

import com.test.demo.model.BenefitAudit;
import com.test.demo.model.SubscriptionAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionAuditRepository extends JpaRepository<SubscriptionAudit, Integer> {

    Page<SubscriptionAudit> findAll(Pageable pageable);
}
