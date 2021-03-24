package com.epam.training.money.impl.domain.orderconfirm.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.domain.orderconfirm.OrderConfirmationService;

public class DummyOrderConfirmationService implements OrderConfirmationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyOrderConfirmationService.class);

    @Override
    public void sendOrderConfirmation(Basket basket) {
        LOGGER.info("An order confirmation for basket {} had been sent.", basket.toString());
    }

    @Override
    public void notify(Basket basket) {
        sendOrderConfirmation(basket);
    }
}
