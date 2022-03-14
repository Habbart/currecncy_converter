package com.example.currency_converter.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class only to perform data transfer from exactly XML format to Currency for further transfer
 */
@XStreamAlias("gesmes:Envelope")
public class CurrencyFromXML {


    @XStreamAlias("gesmes:subject")
    String subject;

    @XStreamAlias("gesmes:Sender")
    Sender sender;

    @XStreamAlias("Cube")
    Cube cube;

    public String getDate() {
        return cube.cubeRate.date;
    }

    public Map<String, String> getMapOfCurrencies() {
        return cube.cubeRate.getMapOfCurrency();
    }


    @XStreamAlias("gesmes:Sender")
    private class Sender {

        @XStreamAlias("gesmes:name")
        String nameOfSender;

        @Override
        public String toString() {
            return "Sender{" +
                    "nameOfSender='" + nameOfSender + '\'' +
                    '}';
        }
    }

    @XStreamAlias("Cube")
    private class Cube {

        @XStreamAlias("Cube")
        CubeRate cubeRate;

        @Override
        public String toString() {
            return "Cube{" +
                    "cubeRate=" + cubeRate +
                    '}';
        }
    }


    @XStreamAlias("Cube")
    private class CubeRate {

        @XStreamAlias("time")
        @XStreamAsAttribute
        private String date;

        @XStreamImplicit(itemFieldName = "Cube")
        List<CubeCurrencyAndRate> listOfCurrency = new ArrayList<>();


        private Map<String, String> getMapOfCurrency() {
            return listOfCurrency.stream().collect(Collectors.toMap(CubeCurrencyAndRate::getCurrency, CubeCurrencyAndRate::getRate));
        }

        @Override
        public String toString() {
            return "CubeRate{" +
                    "date='" + date + '\'' +
                    ", listOfCurrency=" + listOfCurrency +
                    '}';
        }
    }

    @XStreamAlias("Cube")
    private class CubeCurrencyAndRate {

        @XStreamAsAttribute
        @XStreamAlias("currency")
        private String currency;

        @XStreamAsAttribute
        @XStreamAlias("rate")
        private String rate;

        @Override
        public String toString() {
            return "Currency{" +
                    "currency='" + currency + '\'' +
                    ", values='" + rate + '\'' +
                    '}';
        }

        public String getCurrency() {
            return currency;
        }

        public String getRate() {
            return rate;
        }
    }


    @Override
    public String toString() {
        return "CurrencyDto{" +
                "subject='" + subject + '\'' +
                ", sender=" + sender +
                ", cube=" + cube +
                '}';
    }
}
