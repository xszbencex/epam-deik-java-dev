package com.epam.training.money.impl.domain.order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.training.money.impl.repository.OrderRepository;

@Component
public class BasketImpl implements Basket {

    private List<Observer> observers;

    private List<Product> products;
    private List<Coupon> coupons;

    @Autowired
    private OrderRepository orderRepository;

    public BasketImpl() {
        products = new ArrayList<>();
        coupons = new ArrayList<>();
        observers = new LinkedList<>();
    }

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public List<Product> getProductsFromBasket() {
        return products;
    }

    @Override
    public void removeProduct(Product productToRemove) {
        products.remove(productToRemove);
    }

    @Override
    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
    }

    @Override
    public List<Coupon> getCouponsFromBasket() {
        return coupons;
    }

    @Override
    public double getTotalValue() {
        double basePrice = getBasePrice();
        double discount = getDiscountForCoupons();
        return basePrice - discount;
    }

    @Override
    public void order() {
        orderRepository.saveOrder(this);
        observers.forEach(observer -> observer.notify(this));
    }

    @Override
    public double getBasePrice() {
        double basePrice = 0;
        for (Product currentProduct : products) {
            basePrice += currentProduct.getValue();
        }
        return basePrice;
    }

    @Override
    public double getDiscountForCoupons() {
        double discount = 0;
        for (Coupon coupon : coupons) {
            discount += coupon.getDiscountForProducts(products);
        }
        return discount;
    }

    @Override
    public void subscribe(Observer observer) {
        observers.add(observer);
    }
}
