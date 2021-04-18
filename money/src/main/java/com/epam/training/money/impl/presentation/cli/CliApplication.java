package com.epam.training.money.impl.presentation.cli;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.epam.training.money.impl.presentation.cli.configuration.CliConfiguration;

@SpringBootApplication(scanBasePackages = "com.epam.training.money")
public class CliApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CliApplication.class, args);
    }

}
