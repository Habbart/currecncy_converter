package com.example.currency_converter.exception_handler;

public class IllegalCurrency extends RuntimeException {

    public IllegalCurrency(String message) {
        super(message);
    }
}
