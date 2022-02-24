package com.example.currency_converter.util;

import com.example.currency_converter.dto.CurrencyFromXML;
import com.example.currency_converter.entity.Currency;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class CurrencyMapper {
    
    public List<Currency> getCurrencyFromXML(CurrencyFromXML currencyFromXML){
        List<Currency> resultList = new ArrayList<>();
        Map<String, String> currencyAndRate = currencyFromXML.getMapOfCurrencies();
        LocalDate localDate = LocalDate.parse(currencyFromXML.getDate());

        currencyAndRate.forEach((k, v) -> resultList.add(new Currency(localDate, k, Double.parseDouble(v))));

        return resultList;
    } 
}
