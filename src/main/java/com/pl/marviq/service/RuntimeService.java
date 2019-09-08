package com.pl.marviq.service;

import com.pl.marviq.domain.repository.RuntimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;

@Service
public class RuntimeService {

    private BigDecimal ONE_HUNDRED_PERCENT = BigDecimal.valueOf(100);
    private BigDecimal NORM_UPTIME = BigDecimal.valueOf(75);
    private RuntimeRepository runtimeRepository;

    @Autowired
    public RuntimeService(RuntimeRepository runtimeRepository) {
        this.runtimeRepository = runtimeRepository;
    }

    public BigDecimal countMachineCondition(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                            String machineName, boolean isRunning) {
        return runtimeRepository.countMachineCycles(
                dateTimeFrom,
                dateTimeTo,
                machineName,
                isRunning);
    }

    public BigDecimal calculateDowntimePercantage(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                                  String machineName) {
        boolean machineIsRun = true;
        boolean machineIsNotRun = false;

        BigDecimal cyclesWithWork = countMachineCondition(dateTimeFrom, dateTimeTo, machineName, machineIsRun);
        BigDecimal cyclesWithouWork = countMachineCondition(dateTimeFrom, dateTimeTo, machineName, machineIsNotRun);
        BigDecimal allCyclesOfMachine = cyclesWithouWork.add(cyclesWithWork);

        return cyclesWithouWork.divide(allCyclesOfMachine, MathContext.DECIMAL64).multiply(ONE_HUNDRED_PERCENT);
    }

    public BigDecimal calculateUptimePecantage(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                               String machineName) {
        BigDecimal downTimePecantage = calculateDowntimePercantage(dateTimeFrom, dateTimeTo, machineName);
        return ONE_HUNDRED_PERCENT.subtract(downTimePecantage);
    }

    public BigDecimal calculateAvailability(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo,
                                            String machineName) {
        BigDecimal actualUpTime = calculateUptimePecantage(dateTimeFrom, dateTimeTo, machineName);
        return actualUpTime.divide(NORM_UPTIME, MathContext.DECIMAL64).multiply(ONE_HUNDRED_PERCENT);
    }
}
