package com.epam.training.money.impl.presentation.cli.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.presentation.cli.AbstractCommandLineParser;
import com.epam.training.money.impl.repository.ProductRepository;

public class AddProductCommandLineParser extends AbstractCommandLineParser {

    private static final String COMMAND_REGEX = "add product (.+)";
    private ProductRepository productRepository;
    private Basket basket;

    public AddProductCommandLineParser(ProductRepository productRepository, Basket basket) {
        this.productRepository = productRepository;
        this.basket = basket;
    }

    @Override
    protected boolean canCreateCommand(String commandLine) {
        return commandLine.matches(COMMAND_REGEX);
    }

    @Override
    protected Command doCreateCommand(String commandLine) {
        Matcher matcher = Pattern.compile(COMMAND_REGEX).matcher(commandLine);
        matcher.matches();
        String productName = matcher.group(1);
        return new AddProductCommand(productRepository, basket, productName);
    }
}
