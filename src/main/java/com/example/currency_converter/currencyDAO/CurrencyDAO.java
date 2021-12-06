package com.example.currency_converter.currencyDAO;


import com.example.currency_converter.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyDAO extends JpaRepository<Currency, Long> {


    List<Currency> findAllByDate(LocalDate localDate);
    @Query("select c from Currency c where c.name = ?1 group by current_date")
    List<Currency> findAllByName(String name);
    @Query("select c from Currency c order by current_date asc")
    List<Currency> findAlLOrderBy();
    @Query("select distinct c from Currency c where c.date = ?1 and c.name = ?2 group by current_date ")
    List<Currency> findDistinctByDateAndName(LocalDate date, String name);
}
