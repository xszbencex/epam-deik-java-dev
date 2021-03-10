package com.epam.training.money.impl;

import java.util.ArrayList;
import java.util.List;

public class BasketImpl implements Basket {

    private List<Product> products;
    private List<Coupon> coupons;

    public BasketImpl() {
        products = new ArrayList<>();
        coupons = new ArrayList<>();
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
    public double getValuesFromBasket() {
        double valueOfBasket = 0;
        for(Product currentProduct : products) {
            valueOfBasket += currentProduct.getValue();
        }
        return valueOfBasket;
    }
}
