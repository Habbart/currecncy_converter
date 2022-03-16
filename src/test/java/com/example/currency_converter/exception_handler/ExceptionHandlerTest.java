package com.example.currency_converter.exception_handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionHandlerTest {

    ExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ExceptionHandler();
    }

    @Test
    void incorrectDate_shouldReturn_IncorrectDateMessage() {
        IncorrectDate incorrectDate = new IncorrectDate("incorrect date");

        ResponseEntity<String> stringResponseEntity = handler.incorrectDate(incorrectDate);

        String expect = "incorrect date";
        assertEquals(expect, stringResponseEntity.getBody());
    }

    @Test
    void incorrectDate_shouldReturn_400BadRequest() {
        IncorrectDate incorrectDate = new IncorrectDate("incorrect date");

        ResponseEntity<String> stringResponseEntity = handler.incorrectDate(incorrectDate);

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertEquals(expectedStatus, stringResponseEntity.getStatusCode());

    }

    @Test
    void IllegalCurrency_shouldReturn_400BadRequest() {
        IllegalCurrency incorrectCurrency = new IllegalCurrency("illegal currency");

        ResponseEntity<String> stringResponseEntity = handler.incorrectCurrency(incorrectCurrency);

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertEquals(expectedStatus, stringResponseEntity.getStatusCode());

    }
}