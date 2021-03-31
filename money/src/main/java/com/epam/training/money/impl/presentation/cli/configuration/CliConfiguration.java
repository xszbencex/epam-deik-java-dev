package com.epam.training.money.impl.presentation.cli.configuration;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.epam.training.money.impl.domain.order.Basket;
import com.epam.training.money.impl.presentation.cli.CliInterpreter;
import com.epam.training.money.impl.presentation.cli.command.AddProductCommandLineParser;
import com.epam.training.money.impl.presentation.cli.command.CommandLineParser;
import com.epam.training.money.impl.presentation.cli.command.ExitCommandLineParser;
import com.epam.training.money.impl.presentation.cli.command.OrderCommandLineParser;
import com.epam.training.money.impl.repository.ProductRepository;

@Configuration
@ComponentScan("com.epam.training.money")
public class CliConfiguration {

    @Bean
    public CommandLineParser commandLineParserChain(CliInterpreter cliInterpreter, Basket basket, ProductRepository productRepository) {
        CommandLineParser commandLineParserChain = new ExitCommandLineParser(cliInterpreter);
        commandLineParserChain.setSuccessor(new AddProductCommandLineParser(productRepository, basket));
        commandLineParserChain.setSuccessor(new OrderCommandLineParser(basket));
        return commandLineParserChain;
    }

    @Bean
    public Writer cliWriter() {
        return new OutputStreamWriter(System.out);
    }

    @Bean
    public Reader cliReader() {
        return new InputStreamReader(System.in);
    }
}
