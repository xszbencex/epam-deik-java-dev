package com.epam.training.money.impl.presentation.cli;

import java.io.*;

import com.epam.training.money.impl.presentation.cli.command.CommandLineParser;

public class CliInterpreter {

    private BufferedReader input;
    private Writer output;
    private boolean shouldRun;
    private CommandLineParser commandLineParser;

    public CliInterpreter(Reader input, Writer output) {
        this.input = new BufferedReader(input);
        this.output = output;
        commandLineParser = null;
        shouldRun = false;
    }

    public void updateCommandLineParser(CommandLineParser commandLineParser) {
        this.commandLineParser = commandLineParser;
    }

    public void start() throws IOException {
        if (commandLineParser == null) {
            throw new IllegalStateException("Attempted starting the Cli Interpreter without a command line parser");
        }
        shouldRun = true;
        while (shouldRun) {
            String commandLine = input.readLine();
            String result = commandLineParser.createCommand(commandLine).execute();
            output.write(result + System.lineSeparator());
            output.flush();
        }
    }

    public void stop() {
        shouldRun = false;
    }
}
