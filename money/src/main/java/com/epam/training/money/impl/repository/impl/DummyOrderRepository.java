package com.epam.training.money.impl.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.repository.OrderRepository;

public class DummyOrderRepository implements OrderRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyOrderRepository.class);

    @Override
    public void saveOrder(Basket basket) {
        LOGGER.info("Saving order {}", basket);
    }
}
