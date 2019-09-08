package com.pl.marviq.model;

import lombok.Getter;

import java.math.BigDecimal;

public enum ValueOfTemp {
    EIGHTY_FIVE(BigDecimal.valueOf(85)), ONE_HUNDRED(BigDecimal.valueOf(100));

    @Getter
    private BigDecimal temperature;

    ValueOfTemp(BigDecimal temperature) {
        this.temperature = temperature;
    }
}
