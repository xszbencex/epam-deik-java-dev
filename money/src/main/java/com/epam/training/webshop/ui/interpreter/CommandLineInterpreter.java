package com.epam.training.webshop.ui.interpreter;

import com.epam.training.webshop.ui.command.impl.AbstractCommand;
import com.epam.training.webshop.ui.command.impl.BaseCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class CommandLineInterpreter implements AutoCloseable {

    private Scanner scanner;
    private OutputStream output;
    private Set<AbstractCommand> commands;

    public CommandLineInterpreter(InputStream input, OutputStream output, Set<AbstractCommand> commands) {
        Objects.requireNonNull(input, "Input is a mandatory parameter");
        Objects.requireNonNull(output, "Output is a mandatory parameter");
        this.scanner = new Scanner(input);
        this.output = output;
        this.commands = commands;
    }

    public void handleUserInputs() throws IOException {
        String inputLine = null;
        do {
            inputLine = scanner.nextLine();
            if (!"exit".equals(inputLine)) {
                String[] commandParts = inputLine.split(" ");
                if (commandParts.length < 3) {
                    throw new IllegalArgumentException("Invalid command: " + inputLine);
                }
                AbstractCommand abstractCommand = new BaseCommand(commandParts[0], commandParts[1], commandParts[2]);
                Optional<AbstractCommand> optionalCommand = commands.stream().filter(command -> command.equals(abstractCommand)).findFirst();
                if (optionalCommand.isEmpty()) {
                    throw new IllegalArgumentException("Unknown command");
                }
                output.write(optionalCommand.get().process(inputLine).getBytes());
                output.write(System.lineSeparator().getBytes());
            }
        } while (!"exit".equals(inputLine));
    }

    @Override
    public void close() throws Exception {
        scanner.close();
        output.close();
    }

}
