package com.example.currency_converter.controller;


import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.service.CurrencyService;
import com.example.currency_converter.util.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller return list of currency with required parameters
 */

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyMapper currencyMapper;

    /**
     * Method return list of currency with required parameters.
     *
     * @param startDate trying to find currency from this date. If null - find all currencies from this date till current date.
     * @param endDate   trying to find currency until this date. If date is null - find all currencies till current date from start date.
     *                  If both start and end dates are null - return currency for current date.
     * @param currency  - trying to return exact currency. If null - return all currency which available.
     * @return list of currency for range of dates or exact currency
     */

    @GetMapping("")
    public List<CurrencyDto> getCurrency(@RequestParam(value = "start_date", required = false) String startDate,
                                         @RequestParam(value = "end_date", required = false) String endDate,
                                         @RequestParam(value = "currency", required = false) String currency) {

        CurrencyDto currencyDto = currencyMapper.getCurrencyDtoInstance(startDate, endDate, currency);

        return currencyService.getCurrency(currencyDto);
    }

}
