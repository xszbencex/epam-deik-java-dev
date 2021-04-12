package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckoutObservable {

    private List<CheckoutObserver> observers;

    public CheckoutObservable(List<CheckoutObserver> observers) {
        this.observers = observers;
    }

    public void broadcastOrder(Order order) {
        observers.forEach(o -> o.handleOrder(order));
    }

}
