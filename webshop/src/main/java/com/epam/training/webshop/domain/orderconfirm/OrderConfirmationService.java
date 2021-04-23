package com.epam.training.webshop.domain.orderconfirm;

import com.epam.training.webshop.domain.order.Basket;
import com.epam.training.webshop.domain.order.Observer;

public interface OrderConfirmationService extends Observer {
    void sendOrderConfirmation(Basket basket);
}
