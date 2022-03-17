package com.example.currency_converter.service.currency_sub_service;


import com.example.currency_converter.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * Service to download list of currencies from bank to database according to schedule
 */
@AllArgsConstructor
@Component
public class Scheduler {

    private final CurrencyService currencyService;

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 30)
    private void initSchedule() {
        currencyService.saveCurrencyListFromBank();
    }

}
