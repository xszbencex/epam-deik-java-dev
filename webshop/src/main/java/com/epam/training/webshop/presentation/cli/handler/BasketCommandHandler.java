package com.epam.training.webshop.presentation.cli.handler;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.epam.training.webshop.domain.order.Order;
import com.epam.training.webshop.service.OrderService;

/**
 * Command handler for 'order basket' command
 */
@ShellComponent
public class BasketCommandHandler {

    private OrderService orderService;

    public BasketCommandHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @ShellMethod(value = "Orders the basket", key = "order basket")
    public String orderBasket() {
        Order order = orderService.orderCurrentBasket();
        return "Ordered a basket of products worth " + order.getValue() + " HUF.";
    }

    @ShellMethod(value = "Adds a product to the basket", key = "add product")
    public String addProduct(String productName) {
        orderService.addProductToBasket(productName);
        return "Alright.";
    }

}
