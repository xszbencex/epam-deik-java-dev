package com.epam.training.money.impl.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.epam.training.money.impl.domain.order.Product;
import com.epam.training.money.impl.domain.order.SimpleProduct;
import com.epam.training.money.impl.repository.ProductRepository;

@Repository
public class DummyProductRepository implements ProductRepository {
    @Override
    public List<Product> getAllProduct() {
        return List.of(
                new SimpleProduct("Alma", 42),
                new SimpleProduct("Pálinka", 560),
                new SimpleProduct("Táncmulatság", 220));
    }
}
