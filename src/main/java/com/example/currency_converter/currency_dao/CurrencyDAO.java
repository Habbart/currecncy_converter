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

    Currency findFirstByDateBefore(LocalDate localDate);
}
