package com.epam.training.money.impl.presentation.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.training.money.impl.presentation.cli.command.CommandLineParser;

@Service
public class CliInterpreter {

    private BufferedReader input;
    private Writer output;
    private boolean shouldRun;
    private CommandLineParser commandLineParser;

    @Autowired
    public CliInterpreter(Reader input, Writer output) {
        this.input = new BufferedReader(input);
        this.output = output;
        commandLineParser = null;
        shouldRun = false;
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

    @Autowired
    public void setCommandLineParser(CommandLineParser commandLineParser) {
        this.commandLineParser = commandLineParser;
    }
}
