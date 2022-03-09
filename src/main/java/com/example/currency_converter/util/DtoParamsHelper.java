package com.example.currency_converter.util;

import com.example.currency_converter.dto.CurrencyDto;
import com.example.currency_converter.exception_handler.IncorrectDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Class to help work with currency DTO
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoParamsHelper {

    /**
     * Check if String has correct date format to be parse in the future.
     * May return null if it present, so require null check.
     * @param args Dates in String fromat to check
     */
    public static void checkDateFormat(String ... args) {
        if(args == null) return;
        try {
             for (String s: args)   {
                 if(s == null) {
                     continue;
                 }
                    LocalDate.parse(s);
                }
            } catch (Exception e) {
            throw new IncorrectDate("Incorrect date format, date should be in format: yyyy-mm-dd");
            }
        }


    /**
     * Parse dates from DTO.
     * If start date in DTO is null, but end date not null => start date = end date.
     * Opposite, if end date is null, start date is not null => end date = start date.
     * If both dates are null = return today for both dates
     * If start date is after end date - swap dates.
     * @param currencyDto Dto which you receive from Controller
     * @return arrays from two dates - start date and end date
     */
    public static LocalDate[] getStartAndEndDatesFromDto(CurrencyDto currencyDto){
        String startDate = currencyDto.getStartDate();
        String endDate = currencyDto.getEndDate();

        LocalDate[] dates = new LocalDate[2];

        if(startDate == null && endDate == null){
             dates[0] = dates[1] = LocalDate.now();
        } else if(startDate != null && endDate == null){
            dates[0] = dates[1] = LocalDate.parse(startDate);
        } else if(startDate == null){
            dates[0] = dates[1] = LocalDate.parse(endDate);
        } else{
            dates[0] = LocalDate.parse(startDate);
            dates[1] = LocalDate.parse(endDate);
        }
        //если конечная дата раньше чем начальная - то меняем их местами
        if(dates[0].isAfter(dates[1])){
            LocalDate temp = dates[0];
            dates[0] = dates[1];
            dates[1] = temp;
        }

        return dates;
    }
}


