package com.pl.marviq.domain.repository;

import com.pl.marviq.domain.model.ProductionEntity;
import com.pl.marviq.model.VariableName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface ProductionRepository extends CrudRepository<ProductionEntity, Integer> {

    @Query(value = "SELECT SUM (a.value) " +
            "FROM ProductionEntity a " +
            "WHERE a.machineName = :machineName AND a.variableName = :variableName " +
            "AND (a.dateTimeFrom >= :dateTimeFrom AND a.dateTimeTo <= :dateTimeTo)")
    BigDecimal calculateValue(
            @Param("dateTimeFrom") LocalDateTime dateTimeFrom,
            @Param("dateTimeTo") LocalDateTime dateTimeTo,
            @Param("machineName") String machineName,
            @Param("variableName") VariableName variableName);

    @Query(value = "SELECT COUNT(a) " +
            "FROM ProductionEntity a " +
            "WHERE a.variableName = 'CORE TEMPERATURE' AND a.machineName = :machineName " +
            "AND a.value BETWEEN :lowBorder AND :hightBorder " +
            "AND (a.dateTimeFrom >= :dateTimeFrom AND a.dateTimeTo <= :dateTimeTo)")
    Integer countTempMidlPoint(
            @Param("dateTimeFrom") LocalDateTime dateTimeFrom,
            @Param("dateTimeTo") LocalDateTime dateTimeTo,
            @Param("machineName") String machineName,
            @Param("lowBorder") BigDecimal lowBorder,
            @Param("hightBorder") BigDecimal hightBorder
    );
    //I know that use a.variableName = 'CORE TEMPERATURE' it is bad practice(( I have been created enum VariableName
    //and use it everywhere...I can't use there String because Spring Data required for me use Enum...

    @Query(value = "SELECT COUNT(a) " +
            "FROM ProductionEntity a " +
            "WHERE a.variableName = 'CORE TEMPERATURE' AND a.machineName = :machineName " +
            "AND a.value >= :hightBorder " +
            "AND (a.dateTimeFrom >= :dateTimeFrom AND a.dateTimeTo <= :dateTimeTo)")
    Integer countTempMaxPoint(
            @Param("dateTimeFrom") LocalDateTime dateTimeFrom,
            @Param("dateTimeTo") LocalDateTime dateTimeTo,
            @Param("machineName") String machineName,
            @Param("hightBorder") BigDecimal hightBorder
    );
}


