package com.epam.training.webshop.repository;

import com.epam.training.webshop.domain.order.Order;

public interface OrderRepository {
    void saveOrder(Order order);
}
