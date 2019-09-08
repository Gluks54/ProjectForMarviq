package com.pl.marviq.service;

import com.pl.marviq.domain.repository.ProductionRepository;
import com.pl.marviq.model.TempIndicator;
import com.pl.marviq.model.ValueOfTemp;
import com.pl.marviq.model.VariableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductionService {

    static final int ONE_HOUR = 1;
    static final int HOURS_PER_DAY = 24;
    private final int FIFTEEN_MINUTES = 3;
    static final int MINUTES_PER_HOUR = 60;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    private BigDecimal ONE_HUNDRED_PERCENT = BigDecimal.valueOf(100);
    private ProductionRepository productionRepository;
    private BigDecimal NORM_GROSS_PRODUCTION_PER_DAY = BigDecimal.valueOf(30000)
            .multiply(BigDecimal.valueOf(HOURS_PER_DAY));

    @Autowired
    public ProductionService(ProductionRepository productionRepository) {
        this.productionRepository = productionRepository;
    }

    public BigDecimal calculateValue(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                     String machineName, VariableName variableName) {
        return productionRepository
                .calculateValue(
                        dateTimeFrom,
                        dateTimeTo,
                        machineName,
                        variableName);
    }

    public BigDecimal getNetProduction(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                       String machineName) {
        BigDecimal grossValue = calculateValue(dateTimeFrom, dateTimeTo, machineName, VariableName.PRODUCTION);
        BigDecimal scrapValue = calculateValue(dateTimeFrom, dateTimeTo, machineName, VariableName.SCRAP);

        return grossValue.subtract(scrapValue);
    }

    public BigDecimal getScrapPercantage(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                         String machineName) {
        BigDecimal grossValue = calculateValue(dateTimeFrom, dateTimeTo, machineName, VariableName.PRODUCTION);
        BigDecimal scrapValue = calculateValue(dateTimeFrom, dateTimeTo, machineName, VariableName.SCRAP);

        return scrapValue.divide(grossValue, MathContext.DECIMAL64).multiply(ONE_HUNDRED_PERCENT);
    }

    public Map<String, BigDecimal> getNetProdPerHour(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                                     String machineName) {
        Map<String, BigDecimal> tempHashMap = new HashMap<>();
        long hours = getHours(dateTimeFrom, dateTimeTo);

        for (long i = 0; i < hours; i++) {
            if (i >= HOURS_PER_DAY) {
                break;
            }

            String tempString = String.format("H%s", i);

            tempHashMap.put(tempString, getNetProduction(
                    dateTimeFrom,
                    dateTimeFrom.plusHours(ONE_HOUR),
                    machineName));
        }
        return tempHashMap;
    }

    public TempIndicator tempAnalizer(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                      String machineName) {
        int rezultOfMidlPoint = productionRepository.countTempMidlPoint(
                dateTimeFrom,
                dateTimeTo,
                machineName,
                ValueOfTemp.EIGHTY_FIVE.getTemperature(),
                ValueOfTemp.ONE_HUNDRED.getTemperature());

        int rezultOfMaxPoint = productionRepository.countTempMaxPoint(dateTimeFrom,
                dateTimeTo,
                machineName,
                ValueOfTemp.ONE_HUNDRED.getTemperature());

        if (rezultOfMaxPoint > FIFTEEN_MINUTES) {
            return TempIndicator.FATAL_RED;
        }

        if (rezultOfMidlPoint > FIFTEEN_MINUTES) {
            return TempIndicator.WARNING_ORANGE;
        }
        return TempIndicator.GOOD_GREEN;
    }

    private static long getHours(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
        Duration duration = Duration.between(dateTimeFrom, dateTimeTo);
        long seconds = duration.getSeconds();
        return seconds / SECONDS_PER_HOUR;
    }

    public BigDecimal calculatePerformance(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                           String machineName) {
        BigDecimal actualGross = calculateValue(dateTimeFrom, dateTimeTo, machineName, VariableName.PRODUCTION);
        return actualGross.divide(NORM_GROSS_PRODUCTION_PER_DAY, MathContext.DECIMAL64).multiply(ONE_HUNDRED_PERCENT);
    }

    public BigDecimal calculateQuality(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                       String machineName) {
        BigDecimal netProduction = getNetProduction(dateTimeFrom, dateTimeTo, machineName);
        BigDecimal actualGross = calculateValue(dateTimeFrom, dateTimeTo, machineName, VariableName.PRODUCTION);

        return netProduction.divide(actualGross, MathContext.DECIMAL64).multiply(ONE_HUNDRED_PERCENT);
    }
}
