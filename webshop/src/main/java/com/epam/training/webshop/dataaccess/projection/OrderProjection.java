package com.epam.training.webshop.dataaccess.projection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
public class OrderProjection {

    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany
    private List<ProductProjection> products;
    private double value;

    public OrderProjection(List<ProductProjection> products, double value) {
        this.products = products;
        this.value = value;
    }

    protected OrderProjection() {

    }

    public List<ProductProjection> getProducts() {
        return products;
    }

    public double getValue() {
        return value;
    }
}
