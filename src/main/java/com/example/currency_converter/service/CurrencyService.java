package com.example.currency_converter.service;


import com.example.currency_converter.currencyDAO.CurrencyDAO;
import com.example.currency_converter.entity.Currency;
import com.example.currency_converter.exception_handler.IllegalCurrency;
import com.example.currency_converter.exception_handler.IncorrectDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class CurrencyService {

    @Autowired
    private final CurrencyDAO currencyDAO;

    private final static Logger service_logger = LoggerFactory.getLogger("CurrencyService Logger");


    private final RestTemplate restTemplate;

    private List<String> currencyList = new ArrayList<>();

    public CurrencyService(RestTemplateBuilder restTemplateBuilder, CurrencyDAO currencyDAO) throws MalformedURLException {
        this.restTemplate = restTemplateBuilder.build();
        this.currencyDAO = currencyDAO;
    }

    public void getForCurrencyListFromBankAndSave() {
        //take currency from URL
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<String>( headers);
        ResponseEntity<String> response = restTemplate.exchange("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml", HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();
        Document doc = null;
        try {
            doc = loadXMLFromString(responseBody);
        } catch (Exception e){
            service_logger.info("fail to parse loadXMLFromString");
            e.printStackTrace();
        }
        List<Currency> currencyList = parseXMLToCurrency(doc);
        addCurrency(currencyList);


    }


    public List<Currency> getCurrency(String date, String currency) {
        checkParams(date, currency);
        if(currency == null && date != null) {
            LocalDate localDate = LocalDate.parse(date);
            return currencyDAO.findAllByDate(localDate);
        }
        if(date == null && currency != null) return currencyDAO.findAllByName(currency);
        return currencyDAO.findAlLOrderBy().subList(0, 3);

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

    private Document loadXMLFromString(String xml) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private List<Currency> parseXMLToCurrency(Document doc){
        List<Currency> currencyList = new ArrayList<>();
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("Cube");
        String time = "";
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if(time.equals("")) {
                    time = element.getAttribute("time");
                    continue;
                }
                String currency = element.getAttribute("currency");
                String rate = element.getAttribute("rate");
                currencyList.add(new Currency(LocalDate.parse(time), currency, Double.parseDouble(rate)));
                this.currencyList.add(currency);
            }
        }

        return currencyList;
    }
}
