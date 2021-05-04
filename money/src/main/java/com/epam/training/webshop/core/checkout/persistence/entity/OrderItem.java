package com.epam.training.webshop.core.checkout.persistence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private double netPriceAmount;
    private String netPriceCurrencyCode;

    public OrderItem() {

    }

    public OrderItem(Integer id, String name, double netPriceAmount, String netPriceCurrencyCode) {
        this.id = id;
        this.name = name;
        this.netPriceAmount = netPriceAmount;
        this.netPriceCurrencyCode = netPriceCurrencyCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNetPriceAmount() {
        return netPriceAmount;
    }

    public void setNetPriceAmount(double netPriceAmount) {
        this.netPriceAmount = netPriceAmount;
    }

    public String getNetPriceCurrencyCode() {
        return netPriceCurrencyCode;
    }

    public void setNetPriceCurrencyCode(String netPriceCurrencyCode) {
        this.netPriceCurrencyCode = netPriceCurrencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Double.compare(orderItem.netPriceAmount, netPriceAmount) == 0 && Objects.equals(id, orderItem.id) && Objects.equals(name, orderItem.name) && Objects.equals(netPriceCurrencyCode, orderItem.netPriceCurrencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, netPriceAmount, netPriceCurrencyCode);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", netPriceAmount=" + netPriceAmount +
                ", netPriceCurrencyCode='" + netPriceCurrencyCode + '\'' +
                '}';
    }

}
