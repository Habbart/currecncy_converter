package com.example.currency_converter.util;


import com.example.currency_converter.exception_handler.IncorrectDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Check if dates have correct format
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateFormatCheckUtil {

    /**
     * Check if String has correct date format to be parse in the future.
     * May return null if it presents, so require null check.
     *
     * @param args Dates in String format to check
     */
    public static void checkDateFormat(String... args) {
        if (args == null) return;
        String s = null;
        try {
            for (int i = 0; i < args.length; i++) {
                s = args[i];
                if(s == null) continue;
                LocalDate.parse(s);
            }
        } catch (Exception e) {
            throw new IncorrectDate(String.format("Incorrect date format in this date - %s, date should be in format: yyyy-mm-dd", s));
        }
    }
}

