package com.epam.training.webshop.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.epam.training.webshop.dataaccess.dao.OrderDao;
import com.epam.training.webshop.dataaccess.dao.ProductDao;
import com.epam.training.webshop.dataaccess.projection.OrderProjection;
import com.epam.training.webshop.dataaccess.projection.ProductProjection;
import com.epam.training.webshop.domain.order.Order;
import com.epam.training.webshop.domain.order.Product;
import com.epam.training.webshop.repository.OrderRepository;

@Repository
public class JpaOrderRepository implements OrderRepository {

    private OrderDao orderDao;
    private ProductDao productDao;

    public JpaOrderRepository(OrderDao orderDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Override
    public void saveOrder(Order order) {

        orderDao.save(mapOrder(order));
    }

    private OrderProjection mapOrder(Order order) {
        return new OrderProjection(mapProducts(order.getProducts()), order.getValue());
    }

    private List<ProductProjection> mapProducts(List<Product> products) {
        return products.stream()
                .map(this::mapProduct)
                .collect(Collectors.toList());
    }

    private ProductProjection mapProduct(Product product) {
        return productDao.findByName(product.getName())
                .orElseThrow(() -> new IllegalArgumentException("Product projection not found for " + product.getName()));
    }
}
