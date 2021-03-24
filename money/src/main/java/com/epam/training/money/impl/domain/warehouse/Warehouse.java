package com.epam.training.money.impl.domain.warehouse;

import java.util.List;

import com.epam.training.money.impl.domain.order.Observer;
import com.epam.training.money.impl.domain.order.Product;

public interface Warehouse extends Observer {
    void registerOrderedProducts(List<Product> products);
}
