package com.example.currency_converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;



@AllArgsConstructor
@Data
public class CurrencyDto {


    private String startDate;

    private String endDate;

    private String currency;


}
