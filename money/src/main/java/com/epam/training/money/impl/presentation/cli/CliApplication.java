package com.epam.training.money.impl.presentation.cli;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.training.money.impl.presentation.cli.configuration.CliConfiguration;

public class CliApplication {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CliConfiguration.class);
        CliInterpreter cliInterpreter = context.getBean(CliInterpreter.class);
        cliInterpreter.start();
    }

}
