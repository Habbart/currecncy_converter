package com.example.currency_converter.controller;

import com.example.currency_converter.CurrencyConverterApplication;
import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.service.CurrencyService;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CurrencyConverterApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @BeforeAll
    public void init(){
        Currency rub = new Currency(LocalDate.of(2022, 3,1), "RUB", 150.00);
        Currency usd = new Currency(LocalDate.of(2022, 3,1), "USD", 1.15);
        Currency jpy = new Currency(LocalDate.of(2022, 3,1), "JPY", 200.00);
        currencyService.saveListOfCurrencies(List.of(rub, usd, jpy));
    }


    @Test
    void getCurrency_GetRub_thenStatus200() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/currency?currency=rub")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        boolean isCurrencyName = mvcResult.getResponse().getContentAsString().contains("RUB");
        boolean isRatio = mvcResult.getResponse().getContentAsString().contains("150");

        assertTrue(isCurrencyName, "No currency name in response");
        assertTrue(isRatio, "No ratio in response");

    }

    @Test
    void getCurrency_noCurrencyToday_thenStatus200() throws Exception {

        mockMvc.perform(get("/currency?start_date=2022-03-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getCurrency_GetUSDCurrencyToday_thenStatus200() throws Exception {

        mockMvc.perform(get("/currency?currency=USD")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getCurrency_GetRub_thenStatus400() throws Exception {

        mockMvc.perform(get("/currency?currency=rub")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}