package com.epam.training.webshop.domain.order;

import java.util.Objects;

public class SimpleProduct implements Product {

    private final String name;
    private final double value;

    public SimpleProduct(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleProduct that = (SimpleProduct) o;
        return Double.compare(that.value, value) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "SimpleProduct{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
