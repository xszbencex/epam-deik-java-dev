package com.epam.training.webshop.core.checkout.persistence.entity;

import com.epam.training.webshop.core.user.persistence.entity.User;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="`Order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItemList;
    @ManyToOne
    private User user;
    private double netPriceAmount;
    private String netPriceCurrencyCode;
    private double grossPriceAmount;
    private String grossPriceCurrencyCode;

    public Order() {

    }

    public Order(Integer id, List<OrderItem> orderItemList, User user, double netPriceAmount, String netPriceCurrencyCode, double grossPriceAmount, String grossPriceCurrencyCode) {
        this.id = id;
        this.orderItemList = orderItemList;
        this.user = user;
        this.netPriceAmount = netPriceAmount;
        this.netPriceCurrencyCode = netPriceCurrencyCode;
        this.grossPriceAmount = grossPriceAmount;
        this.grossPriceCurrencyCode = grossPriceCurrencyCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public User getUser() {
        return null;
    }

    public void setUser(User user) {
         this.user = user;
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

    public double getGrossPriceAmount() {
        return grossPriceAmount;
    }

    public void setGrossPriceAmount(double grossPriceAmount) {
        this.grossPriceAmount = grossPriceAmount;
    }

    public String getGrossPriceCurrencyCode() {
        return grossPriceCurrencyCode;
    }

    public void setGrossPriceCurrencyCode(String grossPriceCurrencyCode) {
        this.grossPriceCurrencyCode = grossPriceCurrencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.netPriceAmount, netPriceAmount) == 0 && Double.compare(order.grossPriceAmount, grossPriceAmount) == 0 && Objects.equals(id, order.id) && Objects.equals(orderItemList, order.orderItemList) && Objects.equals(user, order.user) && Objects.equals(netPriceCurrencyCode, order.netPriceCurrencyCode) && Objects.equals(grossPriceCurrencyCode, order.grossPriceCurrencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderItemList, user, netPriceAmount, netPriceCurrencyCode, grossPriceAmount, grossPriceCurrencyCode);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderItemList=" + orderItemList +
                ", user=" + user +
                ", netPriceAmount=" + netPriceAmount +
                ", netPriceCurrencyCode='" + netPriceCurrencyCode + '\'' +
                ", grossPriceAmount=" + grossPriceAmount +
                ", grossPriceCurrencyCode='" + grossPriceCurrencyCode + '\'' +
                '}';
    }

}
