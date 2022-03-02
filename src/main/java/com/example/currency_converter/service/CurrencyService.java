package com.example.currency_converter.service;


import com.example.currency_converter.currencyDAO.CurrencyDAO;
import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.exception_handler.IllegalCurrency;
import com.example.currency_converter.exception_handler.IncorrectDate;
import com.example.currency_converter.util.CurrencyMapper;
import com.example.currency_converter.util.TextFileDownloader;
import com.example.currency_converter.util.ValidatorHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
@EnableScheduling
@Slf4j
public class CurrencyService {


    private final TextFileDownloader textFileDownloader;
    private final CurrencyDAO currencyDAO;
    private final CurrencyMapper currencyMapper;

    private List<Currency> currencyCacheList = new ArrayList<>();


    //todo 3.дописать спецификацию
    //todo 4.отдать на ревью


    @Scheduled(timeUnit = TimeUnit.MINUTES ,fixedRate = 30)
    public void saveCurrencyListFromBank() {

        List<Currency> currencyListFromBank = getListOfCurrency();

        saveListOfCurrencies(currencyListFromBank);

    }

    private List<Currency> getListOfCurrency(){

        File file = textFileDownloader.getFile();

        return currencyMapper.getListOfCurrencyFromFile(file);

    }


    /**
     * Return list of currency according to currency dto
     * DTO contains start date, end date and currency.
     * Method trying to find currency from start date to end date.
     * @param currencyDto with start date, end date and currency
     * @return list of currency if present
     */
    public List<CurrencyDto> getCurrency(CurrencyDto currencyDto) {
        //проверяем параметры DTO на валидность
        log.debug("проверяем параметры DTO на валидность");
        ValidatorHelper.checkDateFormat(currencyDto.getStartDate(), currencyDto.getEndDate());
        Currency currency = checkCurrencyAvailability(currencyDto.getCurrency(), currencyCacheList);

        //вытаскиываем переменные для удобства
        LocalDate startDate = getDateFromString(currencyDto.getStartDate());
        LocalDate endDate = getDateFromString(currencyDto.getEndDate());

        log.debug(String.format("start date: %s, end date: %s", startDate, endDate));
        log.debug("берём лист всех валют за выбранные даты");
        //берём лист всех валют за выбранные даты, если даты = null, то возвращает лист за текущую дату
        List<Currency> listOfAllCurrencies = currencyDAO.getAllByDateAfterAndDateBefore(startDate, endDate);

        //если валюта не выбрана, то возвращаем лист всех валют за сегодня, иначе ищет валюту и возвращает лист из одной позиции
        if(currency == null) {
            log.debug("валюта не выбрана");
            if(listOfAllCurrencies.isEmpty()){
                LocalDate availableDate = currencyDAO.findDistinctFirstByIdNotNull().getDate();
                throw new IncorrectDate(String.format("Currency for this date unavailable. Last date of available currency: %s", availableDate));
            }
            return currencyMapper.getListOfCurrencyDto(listOfAllCurrencies);
        } else {
            log.debug("выбрана");
            return List.of(currencyMapper.getCurrencyDto(currency));
        }

    }


    public void saveListOfCurrencies(List<Currency> currencyList){
        currencyDAO.saveAll(currencyList);
    }


    /**
     * Check if list contain exact currency name.
     * If yes - return this currency from list.
     * @param currency name of currency which you want to find in the list
     * @param list where you want to find this currency
     * @return currency if present else - throw exception
     */
    private Currency checkCurrencyAvailability(String currency, List<Currency> list){
        if(currency == null) return null;
        for (Currency c:
                list) {
            currency = currency.toLowerCase().trim();
            String check = c.getName().toLowerCase().trim();
            if(currency.equals(check)) {
                return c;
            }

        }
        throw new IllegalCurrency(String.format("Illegal name of currency: %s", currency));
    }

    //парсим дату из строки, если строка пустая - возвращаем сегодня
    private LocalDate getDateFromString(String dateString){
        if(dateString == null) return LocalDate.now();
        return LocalDate.parse(dateString);
    }



}
