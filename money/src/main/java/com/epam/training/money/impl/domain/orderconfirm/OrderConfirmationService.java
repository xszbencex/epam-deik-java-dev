package com.epam.training.money.impl.domain.orderconfirm;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.domain.order.Observer;

public interface OrderConfirmationService extends Observer {
    void sendOrderConfirmation(Basket basket);
}
