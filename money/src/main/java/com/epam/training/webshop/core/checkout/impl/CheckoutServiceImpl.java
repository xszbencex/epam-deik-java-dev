package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.CheckoutService;
import com.epam.training.webshop.core.checkout.model.Order;
import com.epam.training.webshop.core.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final GrossPriceCalculator grossPriceCalculator;
    private final CheckoutObservable checkoutObservable;

    @Autowired
    public CheckoutServiceImpl(GrossPriceCalculator grossPriceCalculator, CheckoutObservable checkoutObservable) {
        this.grossPriceCalculator = grossPriceCalculator;
        this.checkoutObservable = checkoutObservable;
    }

    @Override
    public Order checkout(Cart cart) {
        Objects.requireNonNull(cart, "Cart cannot be null during checkout process");
        Order order = new Order(cart.getProductList(), cart.getAggregatedNetPrice(), grossPriceCalculator.getAggregatedGrossPrice(cart));
        checkoutObservable.broadcastOrder(order);
        return order;
    }
}
