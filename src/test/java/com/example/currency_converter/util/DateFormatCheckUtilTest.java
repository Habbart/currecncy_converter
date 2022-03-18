package com.example.currency_converter.util;

import com.example.currency_converter.exception_handler.IncorrectDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateFormatCheckUtilTest {




    @Test
    void checkDateFormat(){
        String[] dates = {"2000-09-15", "1000-10-18", "0800-05-30"};

        assertDoesNotThrow(() -> {
            DateFormatCheckUtil.checkDateFormat(dates);
        });

    }

    @Test
    void checkDateFormatShouldThrowException() {
        String[] dates = {"30-11-2020", "asdfasdfasdf", "5-15-2020"};

        assertThrows(IncorrectDate.class, () -> {
            DateFormatCheckUtil.checkDateFormat(dates);
        }, "Should throw exception IncorrectDate for date which not suitable for format: yyyy-mm-dd");

    }

    @Test
    void checkDateFormatGivenNullShouldBeOK(){

        assertDoesNotThrow(() ->{
            DateFormatCheckUtil.checkDateFormat((String) null);
        });
    }

    @Test
    void checkDateFormatGivenNoArgsShouldBeOK(){
        String[] dates = {null, null, null};

        assertDoesNotThrow(() -> {
            DateFormatCheckUtil.checkDateFormat(dates);
        });
    }


}