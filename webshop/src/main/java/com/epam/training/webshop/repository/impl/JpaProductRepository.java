package com.epam.training.webshop.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.training.webshop.dataaccess.dao.ProductDao;
import com.epam.training.webshop.dataaccess.projection.ProductProjection;
import com.epam.training.webshop.domain.order.Product;
import com.epam.training.webshop.domain.order.SimpleProduct;
import com.epam.training.webshop.repository.ProductRepository;

@Repository
public class JpaProductRepository implements ProductRepository {

    private ProductDao productDao;

    @Autowired
    public JpaProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAllProduct() {
        List<ProductProjection> productProjections = productDao.findAll();
        return mapProductProjections(productProjections);
    }

    private List<Product> mapProductProjections(List<ProductProjection> productProjections) {
        return productProjections.stream()
                .map(this::mapProductProjection)
                .collect(Collectors.toList());
    }

    private Product mapProductProjection(ProductProjection productProjection) {
        return new SimpleProduct(productProjection.getName(), productProjection.getValue());
    }
}
