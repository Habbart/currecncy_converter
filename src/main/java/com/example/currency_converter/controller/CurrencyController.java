package com.example.currency_converter.controller;


import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller return list of currency with required parameters
 */

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    /**
     * Method return list of currency with required parameters.
     *
     * @param date - trying to find currency on this date. If null - return currency for last month
     * @param currency - trying to return exact currency from ENUM Allowed currency. If null - return all currency which available.
     * @return list of currency for exact date or exact currency
     */
        @GetMapping("")
        public List<Currency> getCurrency (@RequestParam (value = "date", required = false) String date,
                                           @RequestParam (value = "currency", required = false) String currency){

            return currencyService.getCurrency(date, currency);
        }

}
