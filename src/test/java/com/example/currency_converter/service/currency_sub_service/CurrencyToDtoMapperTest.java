package com.example.currency_converter.service.currency_sub_service;

import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.entity.Currency;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyToDtoMapperTest {


    public static final String END_DATE_NOT_MATCH = "End date didn't match, expected: %s, actual: %s";
    public static final String NAME_NOT_MATCH = "Currency name didn't match, expected: %s, actual: %s";
    public static final String START_DATE = "2020-10-10";
    public static final String END_DATE = "2020-10-15";
    public static final String USD = "usd";

    CurrencyToDtoMapper currencyToDtoMapper;

    public CurrencyToDtoMapperTest() {
        this.currencyToDtoMapper = new CurrencyToDtoMapper();
    }


    @Test
    void getListOfCurrencyDto() {

        Currency rub = new Currency(1, LocalDate.of(2000, 3, 28), "RUB", 150.00);
        Currency usd = new Currency(2, LocalDate.of(2000, 3, 28), "USD", 1.15);
        Currency jpy = new Currency(3, LocalDate.of(2000, 3, 28), "JPY", 200.00);
        Currency usd1 = new Currency(4, LocalDate.of(2000, 3, 29), "USD", 1.16);
        Currency jpy1 = new Currency(5, LocalDate.of(2000, 3, 29), "JPY", 210.00);
        Currency rub1 = new Currency(6, LocalDate.of(2000, 3, 29), "RUB", 160.00);

        List<Currency> currencies = List.of(rub, usd, jpy, usd1, jpy1, rub1);

        List<CurrencyDto> actualOfCurrencyDto = currencyToDtoMapper.getListOfCurrencyDto(currencies);
        String rubels = actualOfCurrencyDto.get(0).getCurrency();
        String jpys = actualOfCurrencyDto.get(2).getCurrency();

        assertEquals(6, actualOfCurrencyDto.size(), "Size of lists didn't match");
        assertFalse(actualOfCurrencyDto.isEmpty(), "List is empty");
        assertTrue(rubels.contains("RUB"), String.format(NAME_NOT_MATCH, "RUB", rubels));
        assertTrue(jpys.contains("JPY"), String.format(NAME_NOT_MATCH, "JPY", jpys));

    }

    @Test
    void getCurrencyDtoInstance() {
        Currency currency = new Currency(LocalDate.of(2020, 10, 15), USD, 1.15);

        CurrencyDto actual = currencyToDtoMapper.getCurrencyDtoInstance(currency);

        assertEquals(END_DATE, actual.getEndDate(), String.format(END_DATE_NOT_MATCH, END_DATE, actual.getEndDate()));
        assertTrue(actual.getCurrency().contains(USD), String.format(NAME_NOT_MATCH, USD, actual.getCurrency()));


    }

    @Test
    void testGetCurrencyDtoInstance() {

        CurrencyDto actual = currencyToDtoMapper.getCurrencyDtoInstance(START_DATE, END_DATE, USD);

        assertEquals(START_DATE, actual.getStartDate(), "Start date didn't match");
        assertEquals(END_DATE, actual.getEndDate(), String.format(END_DATE_NOT_MATCH, END_DATE, actual.getEndDate()));
        assertEquals(USD, actual.getCurrency(), String.format(NAME_NOT_MATCH, USD, actual.getCurrency()));


    }
}