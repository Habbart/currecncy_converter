package com.example.currency_converter.service;

import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.entity.Currency;

import java.util.List;


public interface CurrencyService {

    /**
     * Method which start loading list of currencies from bank and add it to cash and database
     */
    void saveCurrencyListFromBank();

    /**
     * Return list of currency according to currency dto
     * DTO contains start date, end date and currency.
     * Method trying to find currency from start date to end date.
     *
     * @param currencyDto with start date, end date and currency
     * @return list of currency if present
     */
    List<CurrencyDto> getCurrency(CurrencyDto currencyDto);


    /**
     * Save list of currencies to database
     *
     * @param currencyList which you want to save
     */
    void saveListOfCurrencies(List<Currency> currencyList);
}
