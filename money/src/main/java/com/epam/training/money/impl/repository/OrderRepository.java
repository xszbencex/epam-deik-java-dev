package com.epam.training.money.impl.repository;

import com.epam.training.money.impl.domain.order.Basket;

public interface OrderRepository {
    void saveOrder(Basket basket);
}
