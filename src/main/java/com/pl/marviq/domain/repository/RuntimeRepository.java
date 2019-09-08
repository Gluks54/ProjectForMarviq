package com.pl.marviq.domain.repository;

import com.pl.marviq.domain.model.RuntimeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface RuntimeRepository extends CrudRepository<RuntimeEntity, Integer> {

    @Query("SELECT COUNT(a) FROM RuntimeEntity a " +
            "WHERE a.isRunning = :isRunning " +
            "AND  a.machineName = :machineName " +
            "AND (a.dateTime >= :dateTimeFrom AND a.dateTime <= :dateTimeTo)")
    BigDecimal countMachineCycles(
            @Param("dateTimeFrom") LocalDateTime dateTimeFrom,
            @Param("dateTimeTo") LocalDateTime dateTimeTo,
            @Param("machineName") String machineName,
            @Param("isRunning") boolean isRunning
    );
}
