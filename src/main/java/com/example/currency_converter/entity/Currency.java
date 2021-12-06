package com.example.currency_converter.entity;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
