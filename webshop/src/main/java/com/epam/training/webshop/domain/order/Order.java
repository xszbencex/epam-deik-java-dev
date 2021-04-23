package com.epam.training.webshop.domain.order;

import java.util.List;

public class Order {

    private List<Product> products;
    private double value;

    public Order(List<Product> products, double value) {
        this.products = products;
        this.value = value;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getValue() {
        return value;
    }
}
