package com.example.currency_converter.service.currency_sub_service;

import com.example.currency_converter.entity.Currency;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class FileToCurrencyMapperTest {

    public static final String NO_CURRENCY_PRESENT = "This currency doesn't present in the list. Should be: %s";
    public static final String RATION_OF_CURRENCY_NOT_MATCH = "Ratio of currency didn't match. Should be: %s, actual: %s";
    private final FileToCurrencyMapper mapper;
    private File file;

    public FileToCurrencyMapperTest() {
        mapper = new FileToCurrencyMapper();
        init();

    }

    void init() {
        String resourceName = "DownloadedFile_1.txt";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    void getListOfCurrencyFromFile() {

        String date = "2022-03-17";
        String usd = "USD";
        String usdRatio = "1.1051";
        String sek = "SEK";
        String sekRatio = "10.4503";
        String myr = "MYR";
        String myrRatio = "4.6364";

        List<Currency> currencyList = mapper.getListOfCurrencyFromFile(file);
        Optional<Currency> usdOptional = currencyList.stream().filter(x -> x.getName().equals(usd)).findFirst();
        Optional<Currency> sekOptional = currencyList.stream().filter(x -> x.getName().equals(sek)).findFirst();
        Optional<Currency> myrOptional = currencyList.stream().filter(x -> x.getName().equals(myr)).findFirst();

        assertEquals(date, currencyList.get(0).getDate().toString(), "Date didn't match");
        assertEquals(31, currencyList.size(), "Size didn't match");


        assertFalse(usdOptional.isEmpty(),String.format(NO_CURRENCY_PRESENT, usd) );
        assertEquals(usdRatio, usdOptional.get().getRatio().toString(), String.format(RATION_OF_CURRENCY_NOT_MATCH, usdRatio, usdOptional.get().getRatio()));
        assertFalse(sekOptional.isEmpty(), String.format(NO_CURRENCY_PRESENT, sek));
        assertEquals(sekRatio, sekOptional.get().getRatio().toString(), String.format(RATION_OF_CURRENCY_NOT_MATCH, sekRatio, sekOptional.get().getRatio()));
        assertFalse(myrOptional.isEmpty(),  String.format(NO_CURRENCY_PRESENT, myr));
        assertEquals(myrRatio, myrOptional.get().getRatio().toString(), String.format(RATION_OF_CURRENCY_NOT_MATCH, myrRatio, myrOptional.get().getRatio()));
    }
}