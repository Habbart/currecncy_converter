package com.example.currency_converter;

import com.example.currency_converter.controller.CurrencyController;
import com.example.currency_converter.service.CurrencyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CurrencyConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApplication.class, args);

    }

    @Bean
    CommandLineRunner commandLineRunner(CurrencyService currencyService){
        return args -> {
            currencyService.getListOfCurrency();
        };
    }

}
