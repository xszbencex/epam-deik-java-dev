package com.epam.training.money.impl.domain.order;

public interface Observer {
    void notify(Basket basket);
}
