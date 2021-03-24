package com.epam.training.money.impl.presentation.cli.command;

import com.epam.training.money.impl.presentation.cli.AbstractCommandLineParser;
import com.epam.training.money.impl.presentation.cli.CliInterpreter;

public class ExitCommandLineParser extends AbstractCommandLineParser {

    private static final String EXIT_COMMAND = "exit";
    private CliInterpreter cliInterpreterToExitFrom;

    public ExitCommandLineParser(CliInterpreter cliInterpreterToExitFrom) {
        this.cliInterpreterToExitFrom = cliInterpreterToExitFrom;
    }

    @Override
    protected boolean canCreateCommand(String commandLine) {
        return EXIT_COMMAND.equals(commandLine);
    }

    @Override
    protected Command doCreateCommand(String commandLine) {
        return new ExitCommand(cliInterpreterToExitFrom);
    }
}
