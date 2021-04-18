package com.epam.training.money.impl.presentation.cli.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.epam.training.money.impl.command.OrderCommand;
import com.epam.training.money.impl.domain.order.Basket;

/**
 * Command handler for 'order basket' command
 */
@ShellComponent
public class OrderCommandHandler {

    private Basket basket;

    @Autowired
    public OrderCommandHandler(Basket basket) {
        this.basket = basket;
    }

    @ShellMethod(value = "Orders the basket", key = "order basket")
    public String orderBasket() {
        OrderCommand command = new OrderCommand(basket);
        return command.execute();
    }

}
