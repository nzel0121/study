package com.brownfield.pss.search.entity;

import javax.persistence.*;

@Entity
public class Fares {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fare_id")
    private Long id;

    private String fare;
    private String currency;

    public Fares(String fare, String currency) {
        this.fare = fare;
        this.currency = currency;
    }

    public Fares() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
