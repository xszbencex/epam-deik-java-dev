package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckoutObservable {

    private List<CheckoutObserver> observers;

    public CheckoutObservable(List<CheckoutObserver> observers) {
        this.observers = observers;
    }

    public void broadcastOrder(OrderDto orderDto) {
        observers.forEach(o -> o.handleOrder(orderDto));
    }

}
