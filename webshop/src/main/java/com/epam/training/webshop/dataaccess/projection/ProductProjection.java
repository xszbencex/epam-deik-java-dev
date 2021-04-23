package com.epam.training.webshop.dataaccess.projection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class ProductProjection {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private double value;

    public ProductProjection(String name, double value) {
        this.name = name;
        this.value = value;
    }

    protected ProductProjection() {

    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
