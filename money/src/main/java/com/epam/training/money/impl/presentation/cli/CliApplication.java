package com.epam.training.money.impl.presentation.cli;

import java.io.IOException;

public class CliApplication {

    public static void main(String[] args) throws IOException {
        CliConfiguration.cliInterpreter().start();
    }

}
