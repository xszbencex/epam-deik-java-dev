package com.epam.training.webshop.service;

import org.springframework.stereotype.Service;

import com.epam.training.webshop.domain.order.Basket;
import com.epam.training.webshop.domain.order.Order;
import com.epam.training.webshop.domain.order.Product;
import com.epam.training.webshop.repository.OrderRepository;
import com.epam.training.webshop.repository.ProductRepository;

@Service
public class OrderService {

    private Basket basket;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;


    public OrderService(Basket basket, ProductRepository productRepository, OrderRepository orderRepository) {
        this.basket = basket;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void addProductToBasket(String productName) {
        Product productToAdd = productRepository.getAllProduct().stream()
                .filter(currentProduct -> currentProduct.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown product"));
        basket.addProduct(productToAdd);
    }

    public Order orderCurrentBasket() {
        Order order = basket.order();
        orderRepository.saveOrder(order);
        return order;
    }
}
