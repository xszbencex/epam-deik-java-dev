package com.epam.training.webshop.domain.orderconfirm.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.training.webshop.domain.order.Basket;
import com.epam.training.webshop.domain.order.Observable;
import com.epam.training.webshop.domain.orderconfirm.OrderConfirmationService;

@Service
public class DummyOrderConfirmationService implements OrderConfirmationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyOrderConfirmationService.class);

    @Autowired
    public DummyOrderConfirmationService(Observable orderEventSource) {
        orderEventSource.subscribe(this);
    }


    @Override
    public void sendOrderConfirmation(Basket basket) {
        LOGGER.info("An order confirmation for basket {} had been sent.", basket.toString());
    }

    @Override
    public void notify(Basket basket) {
        sendOrderConfirmation(basket);
    }
}
