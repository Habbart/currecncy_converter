package com.example.currency_converter.service.currency_sub_service;


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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Service to map File with XML format to Currency
 * Doesn't work with other types of files.
 */

@Slf4j
@Component
public class FileToCurrencyMapper {


    /**
     * Method push file to exact implementation of parser
     *
     * @param file which you want to parse
     * @return List of currencies
     */
    public List<Currency> getListOfCurrencyFromFile(@NonNull File file) {
        CurrencyFromXML currencyFromXML = parseFileToCurrencyXMLObject(file);
        return getListOfCurrencyFromCurrencyObject(currencyFromXML);
    }

    /**
     * Exact implementation of parsing XML to list of currencies
     * Get XML file and parse it according to CurrencyXML through XStream API.
     * Need to make correct object to parse
     *
     * @param file which we want to parse
     * @return prepared object for further parse
     */
    private CurrencyFromXML parseFileToCurrencyXMLObject(@NonNull File file) {
        //парсим XML файл в CurrencyXML объект
        CurrencyFromXML currencyFromXml = null;
        try (FileReader fileReader = new FileReader(file)) {
            //создаем XStream
            XStream xstream = new XStream();
            // разрешаем всем классам из CurrencyXML читать XStream
            xstream.addPermission(AnyTypePermission.ANY);
            // передаем главный класс
            xstream.processAnnotations(CurrencyFromXML.class);
            currencyFromXml = (CurrencyFromXML) xstream.fromXML(fileReader);

            if (currencyFromXml == null) throw new FileNotFoundException("No XML file, please check URL");
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
     *
     * @param currencyFromXML prepared object for parse
     * @return complete list of Currencies
     */
    private List<Currency> getListOfCurrencyFromCurrencyObject(@NonNull CurrencyFromXML currencyFromXML) {
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
}
