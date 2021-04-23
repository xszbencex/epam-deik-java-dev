package com.epam.training.webshop.domain.warehouse;

import java.util.List;

import com.epam.training.webshop.domain.order.Observer;
import com.epam.training.webshop.domain.order.Product;

public interface Warehouse extends Observer {
    void registerOrderedProducts(List<Product> products);
}
