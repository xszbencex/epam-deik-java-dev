package com.epam.training.money.impl.domain.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class VatProvidingBasketDecorator implements Basket {

    private Basket decorated;
    private double vatRate;

    public VatProvidingBasketDecorator(@Qualifier("basketImpl") Basket decorated, @Value("${VAT_RATE}") double vatRate) {
        this.decorated = decorated;
        this.vatRate = vatRate;
    }

    @Override
    public void addProduct(Product product) {
        decorated.addProduct(product);
    }

    @Override
    public List<Product> getProductsFromBasket() {
        return decorated.getProductsFromBasket();
    }

    @Override
    public void removeProduct(Product productToRemove) {
        decorated.removeProduct(productToRemove);
    }

    @Override
    public void addCoupon(Coupon coupon) {
        decorated.addCoupon(coupon);
    }

    @Override
    public List<Coupon> getCouponsFromBasket() {
        return decorated.getCouponsFromBasket();
    }

    @Override
    public double getTotalValue() {
        return decorated.getTotalValue() * vatRate;
    }

    @Override
    public void order() {
        decorated.order();
    }

    @Override
    public double getBasePrice() {
        return decorated.getBasePrice();
    }

    @Override
    public double getDiscountForCoupons() {
        return decorated.getDiscountForCoupons();
    }

    @Override
    public void subscribe(Observer observer) {
        decorated.subscribe(observer);
    }
}
