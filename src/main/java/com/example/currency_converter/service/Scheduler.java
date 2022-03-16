package com.example.currency_converter.service;


import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@AllArgsConstructor
@Component
public class Scheduler {

    CurrencyService currencyService;

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 30)
    private void initSchedule() {
        currencyService.saveCurrencyListFromBank();
    }

}
