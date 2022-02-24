package com.example.currency_converter.service;


import com.example.currency_converter.currencyDAO.CurrencyDAO;
import com.example.currency_converter.dto.CurrencyFromXML;
import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.exception_handler.IllegalCurrency;
import com.example.currency_converter.exception_handler.IncorrectDate;
import com.example.currency_converter.util.CurrencyMapper;
import com.example.currency_converter.util.TextFileDownloader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@EnableScheduling
public class CurrencyService {


    @Autowired
    private TextFileDownloader textFileDownloader;

    @Autowired
    private final CurrencyDAO currencyDAO;


    private final Logger serviceLogger = LoggerFactory.getLogger("CurrencyService Logger");


    private List<String> currencyList = new ArrayList<>();

    public CurrencyService(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    @Scheduled(timeUnit = TimeUnit.SECONDS ,fixedRate = 30)
    public void getForCurrencyListFromBankAndSave() {
        File file = textFileDownloader.getFile();

        List<Currency> tempListOfCurrency = parseFileToCurrencyDTO(file);

        addCurrency(tempListOfCurrency);

    }

    public void getListOfCurrency(){
        getForCurrencyListFromBankAndSave();
    }


    public List<Currency> getCurrency(String date, String currency) {
        checkParams(date, currency);
        if(currency == null && date != null) {
            LocalDate localDate = LocalDate.parse(date);
            return currencyDAO.findAllByDate(localDate);
        }
        if(date == null && currency != null) return currencyDAO.findAllByName(currency);
        if(date == null) return currencyDAO.findAlLOrderBy().subList(0, 3);
        LocalDate localDate = LocalDate.parse(date);
        return currencyDAO.findDistinctByDateAndName(localDate, currency);

    }


    public void addCurrency(List<Currency> currencyList){
        currencyDAO.saveAll(currencyList);
    }

    private void checkParams(String date, String currency){
        if(date != null) {
            try {
                LocalDate localDate = LocalDate.parse(date);
            } catch (Exception e) {
                throw new IncorrectDate("incorrect date format");
            }
        }
        if(currency != null) {
            if(!currencyList.contains(currency.toUpperCase().trim())){
                throw new IllegalCurrency("illegal currency param");
            }
        }

    }


    private List<Currency> parseFileToCurrencyDTO(File file){
        CurrencyFromXML currencyDto = null;
        try {
            FileReader fileReader = new FileReader(file);
            XStream xstream = new XStream();
            xstream.addPermission(AnyTypePermission.ANY);
            xstream.processAnnotations(CurrencyFromXML.class);
            currencyDto = (CurrencyFromXML) xstream.fromXML(fileReader);
            if(currencyDto == null) throw new FileNotFoundException("No XML file, please check URL");
        } catch (FileNotFoundException e) {
            serviceLogger.warn("need to check XML URL or XML schema");
            e.printStackTrace();
        }

        return new CurrencyMapper().getCurrencyFromXML(currencyDto);
    }
}
