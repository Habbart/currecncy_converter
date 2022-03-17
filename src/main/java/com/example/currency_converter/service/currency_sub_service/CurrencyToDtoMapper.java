package com.example.currency_converter.service.currency_sub_service;

import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.entity.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Class to parse XML file to Currency List
 */
@Slf4j
@Component
public class CurrencyToDtoMapper {


    /**
     * Transform list of currencies to DTO list
     *
     * @param currencies list which you want to transform
     * @return list of DTOs
     */
    public List<CurrencyDto> getListOfCurrencyDto(List<Currency> currencies) {
        return currencies.stream().map(this::getCurrencyDtoInstance).distinct().collect(Collectors.toList());

    }

    /**
     * Method to map currency -> CurrencyDto
     *
     * @param currency to map
     * @return Currency DTO result
     */

    public CurrencyDto getCurrencyDtoInstance(Currency currency) {
        //юзаем stringbuilder для производительности
        StringBuilder sb = new StringBuilder();
        sb.append(currency.getName());
        sb.append(": ");
        sb.append(currency.getRatio());
        return new CurrencyDto(currency.getDate().toString(), currency.getDate().toString(), sb.toString());

    }

    /**
     * Method to map incoming request to Currency DTO
     *
     * @param startDate    from request
     * @param endDate      from request
     * @param currencyName from request
     * @return DTO to further transfer
     */
    public CurrencyDto getCurrencyDtoInstance(String startDate, String endDate, String currencyName) {
        return new CurrencyDto(startDate, endDate, currencyName);

    }
}
