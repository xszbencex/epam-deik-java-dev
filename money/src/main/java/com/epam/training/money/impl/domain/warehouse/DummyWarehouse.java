package com.epam.training.money.impl.domain.warehouse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.domain.order.Product;

public class DummyWarehouse implements Warehouse {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyWarehouse.class);

    @Override
    public void registerOrderedProducts(List<Product> products) {
        LOGGER.info("I have registered the order of products {}", products);
    }

    @Override
    public void notify(Basket basket) {
        registerOrderedProducts(basket.getProductsFromBasket());
    }
}
