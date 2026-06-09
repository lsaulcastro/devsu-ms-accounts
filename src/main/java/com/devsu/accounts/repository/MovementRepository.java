package com.devsu.accounts.repository;

import com.devsu.accounts.domain.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByAccountIdOrderByDateDesc(Long accountId);

    @Query("""
            SELECT m FROM Movement m
            WHERE m.accountId IN :accountIds
              AND m.date >= :fromDate
              AND m.date <= :toDate
            ORDER BY m.date ASC
            """)
    List<Movement> findByAccountIdsAndDateRange(
            @Param("accountIds") List<Long> accountIds,
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate);
}