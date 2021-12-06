package com.example.currency_converter.controller;


import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;


        @GetMapping("/")
        public List<Currency> getCurrency (@RequestParam (value = "date", required = false) String date,
                                           @RequestParam (value = "currency", required = false) String currency){

            return currencyService.getCurrency(date, currency);
        }

}
