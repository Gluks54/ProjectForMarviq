package com.pl.marviq.controllers;

import com.pl.marviq.model.TempIndicator;
import com.pl.marviq.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
public class ProductionController {

    private ProductionService productionService;

    @Autowired
    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/netProduction")
    public ResponseEntity<java.math.BigDecimal> getGrossOut(@RequestParam String dateTimeFrom,
                                                            @RequestParam String dateTimeTo,
                                                            @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        return new ResponseEntity<>(productionService
                .getNetProduction(
                        startDateAndTime,
                        endDateAndTime,
                        machineName), HttpStatus.OK);
    }

    @GetMapping("/scrapPercantage")
    public ResponseEntity<java.math.BigDecimal> getScrapPercantage(@RequestParam String dateTimeFrom,
                                                                   @RequestParam String dateTimeTo,
                                                                   @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        return new ResponseEntity<>(productionService
                .getScrapPercantage(
                        startDateAndTime,
                        endDateAndTime,
                        machineName), HttpStatus.OK);
    }

    @GetMapping("/netPerHour")
    public ResponseEntity<java.util.Map<String,
            java.math.BigDecimal>> getNetPerHour(@RequestParam String dateTimeFrom,
                                                 @RequestParam String dateTimeTo,
                                                 @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        return new ResponseEntity<>(productionService
                .getNetProdPerHour(
                        startDateAndTime,
                        endDateAndTime,
                        machineName), HttpStatus.OK);
    }

    @GetMapping("/tempStatus")
    public ResponseEntity<TempIndicator> getSomething(@RequestParam String dateTimeFrom,
                                                      @RequestParam String dateTimeTo,
                                                      @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        TempIndicator rezult = productionService
                .tempAnalizer(startDateAndTime, endDateAndTime, machineName);

        return new ResponseEntity<>(rezult, HttpStatus.OK);
    }

    @GetMapping("/performance")
    public ResponseEntity<BigDecimal> getPerformance(@RequestParam String dateTimeFrom,
                                                     @RequestParam String dateTimeTo,
                                                     @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        return new ResponseEntity<>(productionService.calculatePerformance(
                startDateAndTime,
                endDateAndTime,
                machineName), HttpStatus.OK);
    }

    @GetMapping("/quality")
    public ResponseEntity<BigDecimal> getQuality(@RequestParam String dateTimeFrom,
                                                 @RequestParam String dateTimeTo,
                                                 @RequestParam String machineName) {

        LocalDateTime startDateAndTime = LocalDateTime.parse(dateTimeFrom);
        LocalDateTime endDateAndTime = LocalDateTime.parse(dateTimeTo);

        return new ResponseEntity<>(productionService.calculateQuality(
                startDateAndTime,
                endDateAndTime,
                machineName), HttpStatus.OK);
    }
}
