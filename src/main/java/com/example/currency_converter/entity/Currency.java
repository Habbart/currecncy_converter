package com.example.currency_converter.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "currencies")
@Setter
@Getter
@ToString
public class Currency {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "name")
    private String name;

    @Column(name = "ratio")
    private Double ratio;

    public Currency() {
    }

    public Currency(LocalDate date, String name, Double ratio) {
        this.date = date;
        this.name = name;
        this.ratio = ratio;
    }

    public Currency(long id, LocalDate date, String name, Double ratio) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.ratio = ratio;
    }

}
