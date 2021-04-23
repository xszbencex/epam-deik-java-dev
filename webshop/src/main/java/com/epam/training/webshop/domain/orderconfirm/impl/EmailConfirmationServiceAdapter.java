package com.epam.training.webshop.domain.orderconfirm.impl;

import com.epam.training.webshop.domain.order.Basket;
import com.epam.training.webshop.domain.orderconfirm.OrderConfirmationService;
import com.epam.training.webshop.domain.orderconfirm.lib.EmailConfirmationService;

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
