package com.epam.training.money.impl.repository;

import java.util.List;

import com.epam.training.money.impl.domain.order.Product;

public interface ProductRepository {
    List<Product> getAllProduct();
}
