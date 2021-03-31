package com.epam.training.money.impl.domain.orderconfirm.impl;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.domain.orderconfirm.OrderConfirmationService;
import com.epam.training.money.impl.domain.orderconfirm.lib.EmailConfirmationService;

public class EmailConfirmationServiceAdapter extends EmailConfirmationService implements OrderConfirmationService {

    @Override
    public void sendOrderConfirmation(Basket basket) {
        this.sendConfirmationMessageAbout(basket.getProductsFromBasket());
    }

    @Override
    public void notify(Basket basket) {
        sendOrderConfirmation(basket);
    }
}
