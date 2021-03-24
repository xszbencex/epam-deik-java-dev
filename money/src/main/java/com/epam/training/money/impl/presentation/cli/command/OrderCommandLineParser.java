package com.epam.training.money.impl.presentation.cli.command;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.presentation.cli.AbstractCommandLineParser;

public class OrderCommandLineParser extends AbstractCommandLineParser {

    private final static String ORDER_COMMAND = "order basket";

    private Basket basketToOrder;

    public OrderCommandLineParser(Basket basketToOrder) {
        this.basketToOrder = basketToOrder;
    }

    @Override
    protected boolean canCreateCommand(String commandLine) {
        return ORDER_COMMAND.equals(commandLine);
    }

    @Override
    protected Command doCreateCommand(String commandLine) {
        return new OrderCommand(basketToOrder);
    }
}
