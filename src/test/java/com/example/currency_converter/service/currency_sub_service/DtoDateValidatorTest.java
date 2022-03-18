package com.example.currency_converter.service.currency_sub_service;

import com.example.currency_converter.dto.CurrencyDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoDateValidatorTest {

    public static final String START_DATE = "2007-10-10";
    public static final String END_DATE = "2008-10-20";
    public static final String USD = "usd";
    public static final String START_DATE_DIDN_T_MATCH = "Start date didn't match, expected: %s, actual: %s";
    public static final String END_DATE_DIDN_T_MATCH = "End date didn't match, expected: %s, actual: %s";
    private final DtoDateValidator dtoDateValidator;


    public DtoDateValidatorTest() {
        this.dtoDateValidator = new DtoDateValidator();
    }

    @Test
    void getStartAndEndDatesFromDto() {
        CurrencyDto currencyDto = new CurrencyDto(START_DATE, END_DATE, USD);

        LocalDate[] dates = dtoDateValidator.getStartAndEndDatesFromDto(currencyDto);

        assertEquals(START_DATE, dates[0].toString(), String.format(START_DATE_DIDN_T_MATCH, START_DATE, dates[0]));
        assertEquals(END_DATE, dates[1].toString(), String.format(END_DATE_DIDN_T_MATCH, END_DATE, dates[1]));
    }

    @Test
    void getStartAndEndDatesFromDtoOnlyEndDate() {
        CurrencyDto currencyDto = new CurrencyDto(null, END_DATE, USD);

        LocalDate[] dates = dtoDateValidator.getStartAndEndDatesFromDto(currencyDto);

        assertEquals(END_DATE, dates[0].toString(), String.format(END_DATE_DIDN_T_MATCH, END_DATE, dates[0]));
        assertEquals(END_DATE, dates[1].toString(), String.format(END_DATE_DIDN_T_MATCH, END_DATE, dates[1]));
    }

    @Test
    void getStartAndEndDatesFromDtoOnlyStartDate() {

        CurrencyDto currencyDto = new CurrencyDto(START_DATE, null, USD);

        LocalDate[] dates = dtoDateValidator.getStartAndEndDatesFromDto(currencyDto);

        assertEquals(START_DATE, dates[0].toString(), String.format(START_DATE_DIDN_T_MATCH, START_DATE, dates[0]));
        assertEquals(START_DATE, dates[1].toString(), String.format(START_DATE_DIDN_T_MATCH, START_DATE, dates[1]));
    }

    @Test
    void getStartAndEndDatesFromDtoNoDates() {

        CurrencyDto currencyDto = new CurrencyDto(null, null, USD);
        String today = LocalDate.now().toString();

        LocalDate[] dates = dtoDateValidator.getStartAndEndDatesFromDto(currencyDto);

        assertEquals(today, dates[0].toString(), String.format(START_DATE_DIDN_T_MATCH, today, dates[0]));
        assertEquals(today, dates[1].toString(), String.format(END_DATE_DIDN_T_MATCH, today, dates[1]));
    }

    @Test
    void getStartAndEndDatesFromDtoMixedUpDates() {

        CurrencyDto currencyDto = new CurrencyDto(END_DATE, START_DATE, USD);

        LocalDate[] dates = dtoDateValidator.getStartAndEndDatesFromDto(currencyDto);

        assertEquals(START_DATE, dates[0].toString(), String.format(START_DATE_DIDN_T_MATCH, START_DATE, dates[0]));
        assertEquals(END_DATE, dates[1].toString(), String.format(END_DATE_DIDN_T_MATCH, END_DATE, dates[1]));
    }
}