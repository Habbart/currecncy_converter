package com.example.currency_converter.service;


import com.example.currency_converter.CurrencyConverterApplication;
import com.example.currency_converter.currency_dao.CurrencyDAO;
import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.exception_handler.IncorrectDate;
import com.example.currency_converter.util.TextFileDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CurrencyConverterApplication.class)
@TestPropertySource(locations = "classpath:application.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private TextFileDownloader textFileDownloader;

    @MockBean
    private CurrencyDAO currencyDAO;

    private final LocalDate startDate = LocalDate.of(2000, 3, 1);
    private final LocalDate endDate = LocalDate.of(2000, 3, 30);
    private final List<Currency> listFromDao = new ArrayList<>();

    @BeforeEach
    void configureCurrencyDAO() {

        Currency rub = new Currency(1, LocalDate.of(2000, 3, 28), "RUB", 150.00);
        Currency usd = new Currency(2, LocalDate.of(2000, 3, 28), "USD", 1.15);
        Currency jpy = new Currency(3, LocalDate.of(2000, 3, 28), "JPY", 200.00);
        Currency usd1 = new Currency(4, LocalDate.of(2000, 3, 29), "USD", 1.16);
        Currency jpy1 = new Currency(5, LocalDate.of(2000, 3, 29), "JPY", 210.00);
        Currency rub1 = new Currency(6, LocalDate.of(2000, 3, 29), "RUB", 160.00);

        listFromDao.addAll(List.of(rub, usd, jpy, usd1, jpy1, rub1));
        //сохраняем весь список, чтобы обновить кэш-лист в Сервисе для последующей валидации
        currencyService.saveListOfCurrencies(listFromDao);

        //настройка мока ДАО
        when(currencyDAO.getAllByDateAfterAndDateBefore(startDate, endDate)).thenReturn(listFromDao);
        when(currencyDAO.getAllByDateAfterAndDateBefore(endDate, startDate)).thenReturn(listFromDao);
        when(currencyDAO.getAllByDateAfterAndDateBefore(startDate, startDate)).thenReturn(Collections.emptyList());
        when(currencyDAO.findFirstByDateBefore(endDate)).thenReturn(listFromDao.get(4));
        when(currencyDAO.findAllByName("USD")).thenReturn(List.of(usd, usd1));
        when(currencyDAO.findFirstByDateBefore(endDate)).thenReturn(rub1);
        when(currencyDAO.findFirstByDateBefore(endDate)).thenReturn(rub1);
        when(currencyDAO.findFirstByDateBefore(startDate)).thenReturn(new Currency(LocalDate.of(2000, 3, 29), "RUB", 160.00));


    }

    @Test
    void saveCurrencyListFromBank() {

        assertThrows(NullPointerException.class, () -> {
            currencyService.saveCurrencyListFromBank();
        });


        Mockito.verify(textFileDownloader, Mockito.times(1)).getFile();

    }

    @Test
    void getCurrencyGivenCorrectDatesAndUSD() {
        CurrencyDto currencyDto = new CurrencyDto(startDate.toString(), endDate.toString(), "USD");

        List<CurrencyDto> currencyDtoList = currencyService.getCurrency(currencyDto);

        assertEquals(2, currencyDtoList.size());
        assertTrue(currencyDtoList.get(0).getCurrency().contains("USD"));

    }

    @Test
    void getCurrencyGivenWrongFormatOfDate_expectException() {
        CurrencyDto currencyDto = new CurrencyDto("aaaa", "bbbb", "USD");
        String message = "Incorrect date format";

        IncorrectDate incorrectDate = assertThrows(IncorrectDate.class, () -> {
            currencyService.getCurrency(currencyDto);
        });

        assertTrue(incorrectDate.getMessage().contains(message));


    }

    @Test
    void getCurrencyGivenDatesWhereNoCurrencyShouldThrowExceptionWithDate() {
        CurrencyDto currencyDto = new CurrencyDto(startDate.toString(), startDate.toString(), "USD");
        String message = "Last date of available currency: 2000-03-29";


        IncorrectDate incorrectDate = assertThrows(IncorrectDate.class, () -> {
            currencyService.getCurrency(currencyDto);
        });

        assertTrue(incorrectDate.getMessage().contains(message));

    }

    @Test
    void getCurrencyGivenNoCurrency_ShouldReturnListOfCurrencies() {
        CurrencyDto currencyDto = new CurrencyDto(startDate.toString(), endDate.toString(), null);
        int expectedSize = 6;


        List<CurrencyDto> currencies = currencyService.getCurrency(currencyDto);


        assertEquals(expectedSize, currencies.size());


    }


    @Test
    void getCurrencyGivenStartDateMixedUpWithEndDate_ShouldReturnUSD() {
        CurrencyDto currencyDto = new CurrencyDto(endDate.toString(), startDate.toString(), "USD");


        List<CurrencyDto> currencyDtoList = currencyService.getCurrency(currencyDto);


        assertEquals(2, currencyDtoList.size());
        assertTrue(currencyDtoList.get(0).getCurrency().contains("USD"));


    }

    @Test
    void saveListOfCurrencies() {
        Currency kzh = new Currency(LocalDate.of(2001, 3, 30), "KZH", 210.00);
        Currency php = new Currency(LocalDate.of(2001, 3, 30), "PHP", 160.00);
        List<Currency> tempList = List.of(kzh, php);
        when(currencyDAO.saveAll(tempList)).thenReturn(null);


        currencyService.saveListOfCurrencies(tempList);


        Mockito.verify(currencyDAO, Mockito.times(1)).saveAll(tempList);

    }


}



