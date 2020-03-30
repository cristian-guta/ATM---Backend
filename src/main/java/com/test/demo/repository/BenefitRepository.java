package com.test.demo.repository;

import com.test.demo.model.Benefit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends CrudRepository<Benefit, Integer> {

    @Query("select b from Benefit b where b.id = ?1")
    Benefit getById(int id);

    @Query("select b from Benefit b where b.description like ?1")
    Benefit findByDescription(String description);

    List<Benefit> findByIdIn(int[] ids);

    @Query("select s.benefits from Subscription s where s.id =?1")
    List<Benefit> findBySubscriptionId(int id);
}
