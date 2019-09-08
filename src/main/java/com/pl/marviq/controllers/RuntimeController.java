package com.pl.marviq.controllers;

import com.pl.marviq.service.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
public class RuntimeController {

    private RuntimeService runtimeService;

    @Autowired
    public RuntimeController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @GetMapping("/downTimePercantage")
    public ResponseEntity<BigDecimal> getDownTimePercantage(@RequestParam String dateTimeFrom,
                                                            @RequestParam String dateTimeTo,
                                                            @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        return new ResponseEntity<>(runtimeService.calculateDowntimePercantage(startDateAndTime,
                endDateAndTime, machineName), HttpStatus.OK);
    }

    @GetMapping("/availability")
    public ResponseEntity<BigDecimal> getAvailability(@RequestParam String dateTimeFrom,
                                                      @RequestParam String dateTimeTo,
                                                      @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        return new ResponseEntity<>(runtimeService.calculateAvailability(
                startDateAndTime,
                endDateAndTime,
                machineName), HttpStatus.OK);
    }
}
