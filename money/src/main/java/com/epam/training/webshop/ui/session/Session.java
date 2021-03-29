package com.epam.training.webshop.ui.session;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.finance.bank.staticbank.impl.StaticBank;
import com.epam.training.webshop.core.finance.bank.staticbank.model.StaticExchangeRates;

public enum Session {

    INSTANCE;

    private Cart cart = new Cart(StaticBank.of(() -> new StaticExchangeRates.Builder()
            .addRate("HUF", "USD", 0.0034, 249.3)
            .build()));

    public Cart getCart() {
        return cart;
    }

}
