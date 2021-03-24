package com.epam.training.money.impl.presentation.cli;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.domain.order.BasketImpl;
import com.epam.training.money.impl.domain.orderconfirm.OrderConfirmationService;
import com.epam.training.money.impl.domain.orderconfirm.impl.DummyOrderConfirmationService;
import com.epam.training.money.impl.domain.warehouse.DummyWarehouse;
import com.epam.training.money.impl.domain.warehouse.Warehouse;
import com.epam.training.money.impl.presentation.cli.command.AddProductCommandLineParser;
import com.epam.training.money.impl.presentation.cli.command.CommandLineParser;
import com.epam.training.money.impl.presentation.cli.command.ExitCommandLineParser;
import com.epam.training.money.impl.presentation.cli.command.OrderCommandLineParser;
import com.epam.training.money.impl.repository.OrderRepository;
import com.epam.training.money.impl.repository.ProductRepository;
import com.epam.training.money.impl.repository.impl.DummyOrderRepository;
import com.epam.training.money.impl.repository.impl.DummyProductRepository;

public class CliConfiguration {

    public static CliInterpreter cliInterpreter() {
        Basket basket = basket();
        CliInterpreter cliInterpreter = new CliInterpreter(cliReader(), cliWriter());
        CommandLineParser commandLineParserChain = new ExitCommandLineParser(cliInterpreter);
        commandLineParserChain.setSuccessor(new AddProductCommandLineParser(productRepository(), basket));
        commandLineParserChain.setSuccessor(new OrderCommandLineParser(basket));
        cliInterpreter.updateCommandLineParser(commandLineParserChain);
        return cliInterpreter;
    }

    public static Writer cliWriter() {
        return new OutputStreamWriter(System.out);
    }

    public static Reader cliReader() {
        return new InputStreamReader(System.in);
    }

    public static Basket basket() {

        BasketImpl basket = new BasketImpl(orderRepository());
        basket.subscribe(warehouse());
        basket.subscribe(orderConfirmationService());
        return basket;
    }

    public static ProductRepository productRepository() {
        return new DummyProductRepository();
    }

    public static OrderRepository orderRepository() {
        return new DummyOrderRepository();
    }

    public static Warehouse warehouse() {
        return new DummyWarehouse();
    }

    public static OrderConfirmationService orderConfirmationService() {
        return new DummyOrderConfirmationService();
    }
}
