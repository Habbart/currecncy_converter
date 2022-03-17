package com.example.currency_converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * To this class mapped incoming requests
 */
@AllArgsConstructor
@Data
public class CurrencyDto {

    private final String startDate;
    private final String endDate;
    private final String currency;


}
