package com.epam.training.webshop;

import com.epam.training.webshop.ui.configuration.ApplicationConfiguration;
import com.epam.training.webshop.ui.interpreter.CommandLineInterpreter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        CommandLineInterpreter commandLineInterpreter = context.getBean(CommandLineInterpreter.class);
        commandLineInterpreter.handleUserInputs();
    }

}
