package com.epam.training.webshop.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.epam.training.webshop.domain.order.Order;
import com.epam.training.webshop.repository.OrderRepository;

public class DummyOrderRepository implements OrderRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyOrderRepository.class);

    @Override
    public void saveOrder(Order order) {
        LOGGER.info("Saving order {}", order);
    }
}
