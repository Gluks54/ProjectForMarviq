package com.pl.marviq.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Runtime")
public class RuntimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "machine_name")
    private String machineName;

    @Column(name = "datetime")
    private LocalDateTime dateTime;

    @Column(name = "isrunning")
    private boolean isRunning;
}
