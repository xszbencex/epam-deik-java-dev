package com.epam.training.webshop.repository;

import java.util.List;

import com.epam.training.webshop.domain.order.Product;

public interface ProductRepository {
    List<Product> getAllProduct();
}
