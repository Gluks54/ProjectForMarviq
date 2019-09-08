package com.pl.marviq.domain.model;

import com.pl.marviq.model.VariableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Production")
public class ProductionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "machine_name")
    private String machineName;

    @Column(name = "variable_name")
    @Enumerated(EnumType.STRING)
    private VariableName variableName;


    @Column(name = "datetime_from")
    private LocalDateTime dateTimeFrom;


    @Column(name = "datetime_to")
    private LocalDateTime dateTimeTo;

    @Column(name = "value")
    private BigDecimal value;
}
