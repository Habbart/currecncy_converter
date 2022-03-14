package com.example.currency_converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * To this class mapped incoming requests
 */
@AllArgsConstructor
@Data
public class CurrencyDto {


    private String startDate;

    private String endDate;

    private String currency;


}
