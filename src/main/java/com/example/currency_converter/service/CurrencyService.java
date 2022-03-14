package com.example.currency_converter.service;


import com.example.currency_converter.currency_dao.CurrencyDAO;
import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.exception_handler.IllegalCurrency;
import com.example.currency_converter.exception_handler.IncorrectDate;
import com.example.currency_converter.util.CurrencyMapper;
import com.example.currency_converter.util.DtoParamsHelper;
import com.example.currency_converter.util.TextFileDownloader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@EnableScheduling
@Slf4j
public class CurrencyService {


    private final TextFileDownloader textFileDownloader;
    private final CurrencyDAO currencyDAO;
    private final CurrencyMapper currencyMapper;
    /**
     * This set provide check of available currencies.
     * Each time when currency will be added - it will be added also in this cash.
     * HashSet guarantee no duplicates so cash shouldn't be so big.
     */
    private HashSet<String> currencyCashSet;


    //todo 3.дописать спецификацию
    //todo 4.отдать на ревью
    //todo 5. переписать загрузку файла в асинхрон


    /**
     * Method which start loading list of currencies from bank and add it to cash and database
     */
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 30)
    public void saveCurrencyListFromBank() {

        List<Currency> currencyListFromBank = getListOfCurrency();

        saveListOfCurrencies(currencyListFromBank);

    }

    /**
     * Method get file from File downloader and push it in currency mapper.
     * Also update list of available currencies.
     *
     * @return list of Currencies
     */

    private List<Currency> getListOfCurrency() {

        File file = textFileDownloader.getFile();

        List<Currency> listOfCurrencyFromFile = currencyMapper.getListOfCurrencyFromFile(file);

        updateCashOfCurrenciesNames(listOfCurrencyFromFile);

        return listOfCurrencyFromFile;

    }


    /**
     * Return list of currency according to currency dto
     * DTO contains start date, end date and currency.
     * Method trying to find currency from start date to end date.
     *
     * @param currencyDto with start date, end date and currency
     * @return list of currency if present
     */
    public List<CurrencyDto> getCurrency(CurrencyDto currencyDto) {

        log.debug("проверяем параметры DTO на валидность");
        DtoParamsHelper.checkDateFormat(currencyDto.getStartDate(), currencyDto.getEndDate());
        LocalDate[] dates = DtoParamsHelper.getStartAndEndDatesFromDto(currencyDto);
        String currencyName = checkCurrencyAvailability(currencyDto.getCurrency(), currencyCashSet);

        //вытаскиываем переменные для удобства
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];

        log.debug(String.format("start date: %s, end date: %s", startDate, endDate));
        log.debug("берём лист всех валют за выбранные даты");

        //берём лист всех валют за выбранные даты, если даты = null, то возвращает лист за текущую дату
        List<Currency> listOfAllCurrencies = currencyDAO.getAllByDateAfterAndDateBefore(startDate, endDate);

        //если за выбранные даты не найдено ни одной валюты, то кидаем исключение с ближайшей доступной датой для потворного запроса
        if (listOfAllCurrencies.isEmpty()) {
            LocalDate availableDate = currencyDAO.findDistinctFirstByDateBefore(endDate).getDate();
            throw new IncorrectDate(String.format("Currency %S for this date unavailable. Last date of available currency: %s", currencyDto.getCurrency(), availableDate));
        }

        //если валюта не выбрана, то возвращаем лист всех валют за выбранные даты, иначе ищет валюту и возвращает лист из одной позиции
        if (currencyName.isEmpty()) {
            log.debug("валюта не выбрана");
            return currencyMapper.getListOfCurrencyDto(listOfAllCurrencies);
        } else {
            log.debug("выбрана");
            log.debug(currencyName);
            //фильтруем лист, где совпадает валюта с переданной и возвращаем
            List<Currency> collect = listOfAllCurrencies.stream().filter(c -> c.getName().equals(currencyName)).collect(Collectors.toList());
            return currencyMapper.getListOfCurrencyDto(collect);
        }

    }

    /**
     * Save list of currencies to database.
     * also update cash with available currencies
     * Should be public because of test reason
     *
     * @param currencyList which you want to save
     */

    public void saveListOfCurrencies(List<Currency> currencyList) {
        updateCashOfCurrenciesNames(currencyList);
        currencyDAO.saveAll(currencyList);
    }


    /**
     * Check if list contain exact currency name.
     * If yes - return this currency from list.
     *
     * @param currency   name of currency which you want to find in the list
     * @param currencies where you want to find this currency
     * @return currency if present else - throw exception
     */
    private String checkCurrencyAvailability(String currency, Collection<String> currencies) {
        if (currency == null) return "";
        for (String s :
                currencies) {
            currency = currency.toLowerCase().trim();
            String check = s.toLowerCase().trim();
            if (currency.equals(check)) {
                return s;
            }

        }
        throw new IllegalCurrency(String.format("Illegal name of currency: %s", currency));
    }

    /**
     * Method transform list to hashset
     *
     * @param list which you want to transform
     */
    private void updateCashOfCurrenciesNames(List<Currency> list) {
        list.forEach(c -> currencyCashSet.add(c.getName()));
    }


}
