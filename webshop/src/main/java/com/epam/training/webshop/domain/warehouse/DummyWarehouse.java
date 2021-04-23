package com.epam.training.webshop.domain.warehouse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.epam.training.webshop.domain.order.Basket;
import com.epam.training.webshop.domain.order.Observable;
import com.epam.training.webshop.domain.order.Product;

@Service
public class DummyWarehouse implements Warehouse {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyWarehouse.class);

    public DummyWarehouse(Observable orderEventSource) {
        orderEventSource.subscribe(this);
    }

    @Override
    public void registerOrderedProducts(List<Product> products) {
        LOGGER.info("I have registered the order of products {}", products);
    }

    @Override
    public void notify(Basket basket) {
        registerOrderedProducts(basket.getProductsFromBasket());
    }
}
