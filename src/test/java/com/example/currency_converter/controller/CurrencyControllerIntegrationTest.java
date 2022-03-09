package com.example.currency_converter.controller;

import com.example.currency_converter.CurrencyConverterApplication;
import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CurrencyConverterApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @BeforeAll
    public void init(){
        currencyService.saveCurrencyListFromBank();
        Currency rub = new Currency(LocalDate.of(2000, 3,28), "RUB", 150.00);
        Currency usd = new Currency(LocalDate.of(2000, 3,28), "USD", 1.15);
        Currency usd2 = new Currency(LocalDate.of(2000, 3,29), "USD", 1.16);
        Currency jpy = new Currency(LocalDate.of(2000, 3,28), "JPY", 200.00);
        Currency jpy2 = new Currency(LocalDate.of(2000, 3,29), "JPY", 210.00);
        Currency rub2 = new Currency(LocalDate.of(2000, 3,29), "RUB", 160.00);
        currencyService.saveListOfCurrencies(List.of(rub, usd, jpy, usd2, jpy2, rub2));
    }


    @Test
    void getCurrency_GetUsd_thenExpectNameIsPresent() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/currency?currency=rub")
                        .accept(MediaType.APPLICATION_JSON));


        MvcResult mvcResult = resultActions.andReturn();
        String content =  mvcResult.getResponse().getContentAsString();
        boolean isCurrencyName = content.contains("RUB");

        assertTrue(isCurrencyName, "No currency name in response");

    }

    @Test
    void getCurrency_noCurrencyToday_thenStatus200() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/currency?start_date=2000-03-28")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String content =  mvcResult.getResponse().getContentAsString();


        boolean isCurrencyName = content.contains("USD");
        boolean isStartDateSame = content.contains("2000-03-28");
        boolean isRation150 = content.contains("150");

        assertTrue(isCurrencyName, "No currency name in response");
        assertTrue(isStartDateSame, "Doesnt' return date of request");
        assertTrue(isRation150, "Doesn't return saved ratio");

    }

    @Test
    void getCurrency_GetUSDCurrencyForRangeOfDates_thenStatus200() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/currency?start_date=2000-03-27&end_date=2000-03-30&currency=usd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String content =  mvcResult.getResponse().getContentAsString();
        log.info(content);
        boolean isCurrencyName = content.contains("USD");
        boolean isStartDateSame = content.contains("2000-03-28");
        boolean isDatePresent = content.contains("2000-03-29");
        boolean isRation150 = content.contains("1.15");

        assertTrue(isCurrencyName, "No currency name in response");
        assertTrue(isStartDateSame, "Doesnt' return correct start date of request");
        assertTrue(isDatePresent, "Doesnt' return correct date of currency");
        assertTrue(isRation150, "Doesn't return saved ratio");

    }

    @Test
    void getCurrency_GetRub_thenStatus200() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/currency?currency=usd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        MvcResult mvcResult = resultActions.andReturn();
        boolean isContainCurrencyName = mvcResult.getResponse().getContentAsString().contains("USD");

        assertTrue(isContainCurrencyName);

    }

    @Test
    void getCurrency_noDate_noCurrency_thenBadRequest() throws Exception {

        mockMvc.perform(get("/currency?")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}