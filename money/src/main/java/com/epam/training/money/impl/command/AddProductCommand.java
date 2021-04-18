package com.epam.training.money.impl.command;

import java.util.Optional;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.domain.order.Product;
import com.epam.training.money.impl.repository.ProductRepository;

public class AddProductCommand implements Command {

    private ProductRepository productRepository;
    private Basket basket;
    private String productNameToAdd;

    public AddProductCommand(ProductRepository productRepository, Basket basket, String productNameToAdd) {
        this.productRepository = productRepository;
        this.basket = basket;
        this.productNameToAdd = productNameToAdd;
    }

    @Override
    public String execute() {
        Optional<Product> productToAdd = productRepository.getAllProduct()
                .stream()
                .filter(product -> product.getName().equals(productNameToAdd))
                .findFirst();
        if (productToAdd.isEmpty()) {
            return "No such product";
        }
        basket.addProduct(productToAdd.get());
        return "Alright.";
    }
}
