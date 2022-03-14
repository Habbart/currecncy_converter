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


    /**
     * Method push file to exact implementation of parser
     * @param file which you want to parse
     * @return List of currencies
     */
    public List<Currency> getListOfCurrencyFromFile(@NonNull File file){
        CurrencyFromXML currencyFromXML = parseFileToCurrencyXMLObject(file);
        return getListOfCurrencyFromCurrencyObject(currencyFromXML);
    }

    /**
     * Exact implementation of parsing XML to list of currencies
     * Get XML file and parse it according to CurrencyXML through XStream API.
     * Need to make correct object to parse
     * @param file which we want to parse
     * @return prepared object for further parse
     */
    private CurrencyFromXML parseFileToCurrencyXMLObject(@NonNull File file){
        //парсим XML файл в CurrencyXML объект
        CurrencyFromXML currencyFromXml = null;
        try (FileReader fileReader = new FileReader(file)){
            //создаем XStream
                XStream xstream = new XStream();
            // разрешаем всем классам из CurrencyXML читать XStream
                xstream.addPermission(AnyTypePermission.ANY);
            // передаем главный класс
                xstream.processAnnotations(CurrencyFromXML.class);
                currencyFromXml = (CurrencyFromXML) xstream.fromXML(fileReader);

            if(currencyFromXml == null) throw new FileNotFoundException("No XML file, please check URL");
        } catch (FileNotFoundException e) {
            log.warn("need to check XML URL or XML schema");
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("filereader didn't closed");
            e.printStackTrace();
        }
        return currencyFromXml;
    }

    /**
     * Get list of Currency from prepared Object which we parse from XML file
     * @param currencyFromXML prepared object for parse
     * @return complete list of Currencies
     */
    private List<Currency> getListOfCurrencyFromCurrencyObject(@NonNull CurrencyFromXML currencyFromXML){
        //получаем список валюты из объекта, в который мы парсили XML
        List<Currency> resultList = new ArrayList<>();
        //возвращаем Мапу состояющую из имени валюты и ставки
        Map<String, String> currencyAndRate = currencyFromXML.getMapOfCurrencies();
        //парсим дату из объекта, она одна на весь объект
        LocalDate localDate = LocalDate.parse(currencyFromXML.getDate());
        //создаем лист из мапы
        currencyAndRate.forEach((k, v) -> resultList.add(new Currency(localDate, k, Double.parseDouble(v))));

        return resultList;
    }

    /**
     * Transform list of curriencies to DTO list
     * @param currencies list which you want to transform
     * @return list of DTOs
     */
    public List<CurrencyDto> getListOfCurrencyDto(List<Currency> currencies){
        return currencies.stream().map(this::getCurrencyDtoInstance).distinct().collect(Collectors.toList());

    }

    /**
     * Method to map currency -> CurrencyDto
     * @param currency to map
     * @return Currency DTO result
     */

    public CurrencyDto getCurrencyDtoInstance(Currency currency){
        //юзаем stringbuilder для производительности
        StringBuilder sb = new StringBuilder();
        sb.append(currency.getName());
        sb.append(": ");
        sb.append(currency.getRatio());
        return new CurrencyDto(currency.getDate().toString(), currency.getDate().toString(), sb.toString());

    }

    /**
     * Method to map incoming request to Currency DTO
     * @param startDate from request
     * @param endDate from request
     * @param currencyName from request
     * @return DTO to further transfer
     */
    public CurrencyDto getCurrencyDtoInstance(String startDate, String endDate, String currencyName){
        return new CurrencyDto(startDate, endDate, currencyName);

    }
}
