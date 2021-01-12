package com.test.demo.repository;

import com.test.demo.model.Benefit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Integer> {

    @Query("select b from Benefit b where b.id = ?1")
    Benefit getById(int id);

    @Query("select b from Benefit b where b.description like ?1")
    Benefit findByDescription(String description);

    List<Benefit> findByIdIn(int[] ids);

    @Query("select s.benefits from Subscription s where s.id =?1")
    List<Benefit> findBySubscriptionId(int id);

    @Query(value = "select * from clientdb.benefit b " +
            "join subscriptions_benefits sb on b.id = sb.benefit_id " +
            "join subscription s on sb.subscription_id = s.id " +
            "where s.id=?1",
            countQuery = "select count(*) from clientdb.benefit b " +
                    "join subscriptions_benefits sb on b.id = sb.benefit_id " +
                    "join subscription s on sb.subscription_id = s.id " +
                    "where s.id=?1",
            nativeQuery = true)
    Page<Benefit> findPagedBySubId(int id, Pageable pageable);


}
