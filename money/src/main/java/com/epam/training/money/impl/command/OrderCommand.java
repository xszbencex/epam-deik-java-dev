package com.epam.training.money.impl.command;

import com.epam.training.money.impl.domain.order.Basket;

public class OrderCommand implements Command {

    private Basket basketToOrder;

    public OrderCommand(Basket basketToOrder) {
        this.basketToOrder = basketToOrder;
    }

    @Override
    public String execute() {
        double value = basketToOrder.getTotalValue();
        basketToOrder.order();
        return "Ordered a basket worth " + value;
    }
}
