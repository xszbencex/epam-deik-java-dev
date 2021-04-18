package com.epam.training.money.impl.presentation.cli.handler;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.epam.training.money.impl.command.AddProductCommand;
import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.repository.ProductRepository;

@ShellComponent
public class AddProductHandler {

    private Basket basket;
    private ProductRepository productRepository;

    public AddProductHandler(Basket basket, ProductRepository productRepository) {
        this.basket = basket;
        this.productRepository = productRepository;
    }


    @ShellMethod(value = "Adds a product to the basket", key = "add product")
    public String addProduct(String productName) {
        AddProductCommand command = new AddProductCommand(productRepository, basket, productName);
        return command.execute();
    }

}
