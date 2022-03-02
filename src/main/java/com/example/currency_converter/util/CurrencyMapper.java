package com.example.currency_converter.util;

import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.dto.CurrencyFromXML;
import com.example.currency_converter.entity.Currency;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Class to parse XML file to Currency List
 */
@Slf4j
@Component
public class CurrencyMapper {


    public List<Currency> getListOfCurrencyFromFile(@NonNull File file){
        CurrencyFromXML currencyFromXML = parseFileToCurrencyXMLObject(file);
        return getListOfCurrencyFromCurrencyObject(currencyFromXML);
    }

    /**
     * Get XML file and parse it according to CurrencyXML through XStream API.
     * Need to make correct object to parse
     * @param file which we want to parse
     * @return prepared object for further parse
     */
    private CurrencyFromXML parseFileToCurrencyXMLObject(@NonNull File file){
        //парсим XML файл в CurrencyXML объект
        CurrencyFromXML сurrencyFromXML = null;
        try (FileReader fileReader = new FileReader(file)){
                XStream xstream = new XStream(); //создаем XStream
                xstream.addPermission(AnyTypePermission.ANY); // разрешаем всем классам из CurrencyXML читать XStream
                xstream.processAnnotations(CurrencyFromXML.class); // передаем главный класс
                сurrencyFromXML = (CurrencyFromXML) xstream.fromXML(fileReader);

            if(сurrencyFromXML == null) throw new FileNotFoundException("No XML file, please check URL");
        } catch (FileNotFoundException e) {
            log.warn("need to check XML URL or XML schema");
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("filereader didn't closed");
            e.printStackTrace();
        }
        return сurrencyFromXML;
    }

    /**
     * Get list of Currency from prepared Object which we parse from XML file
     * @param currencyFromXML prepared object for parse
     * @return complete list of Currencies
     */
    private List<Currency> getListOfCurrencyFromCurrencyObject(@NonNull CurrencyFromXML currencyFromXML){
        //получаем список валюты из объекта, в который мы парсили XML
        List<Currency> resultList = new ArrayList<>();
        Map<String, String> currencyAndRate = currencyFromXML.getMapOfCurrencies();
        LocalDate localDate = LocalDate.parse(currencyFromXML.getDate());

        currencyAndRate.forEach((k, v) -> resultList.add(new Currency(localDate, k, Double.parseDouble(v))));

        return resultList;
    }

    /**
     * Transform list of curriencies to DTO list
     * @param currencies list which you want to transform
     * @return list of DTOs
     */
    public List<CurrencyDto> getListOfCurrencyDto(List<Currency> currencies){
        return currencies.stream().map(this::getCurrencyDto).distinct().collect(Collectors.toList());

    }

    public CurrencyDto getCurrencyDto(Currency currency){
        StringBuilder sb = new StringBuilder();
        sb.append(currency.getName());
        sb.append(": ");
        sb.append(currency.getRatio());
        return new CurrencyDto(currency.getDate().toString(), currency.getDate().toString(), sb.toString());

    }
}
