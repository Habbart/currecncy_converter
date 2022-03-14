package com.example.currency_converter.currency_dao;


import com.example.currency_converter.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyDAO extends JpaRepository<Currency, Long> {

    @Query("select c from Currency c where c.date >= ?1 and c.date <= ?2")
    List<Currency> getAllByDateAfterAndDateBefore(LocalDate startDate, LocalDate beforeDate);

    @Query("select c from Currency c where c.name = ?1 group by current_date")
    List<Currency> findAllByName(String name);

    @Query("select c from Currency c order by current_date asc")
    List<Currency> findAlLOrderBy();

    @Query("select distinct c from Currency c where c.date = ?1 and c.name = ?2 group by current_date ")
    List<Currency> findDistinctByDateAndName(LocalDate date, String name);

    @Query("select (count(c) > 0) from Currency c where c.name = ?1")
    boolean existsCurrencyByName(String name);


    Currency findDistinctFirstByDateBefore(LocalDate localDate);
}
