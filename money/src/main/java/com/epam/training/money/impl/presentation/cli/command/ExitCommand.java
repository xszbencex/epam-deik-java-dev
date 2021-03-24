package com.epam.training.money.impl.presentation.cli.command;

import com.epam.training.money.impl.presentation.cli.CliInterpreter;

public class ExitCommand implements Command {

    private CliInterpreter cliInterpreterToExitFrom;

    public ExitCommand(CliInterpreter cliInterpreterToExitFrom) {
        this.cliInterpreterToExitFrom = cliInterpreterToExitFrom;
    }

    @Override
    public String execute() {
        cliInterpreterToExitFrom.stop();
        return "Exiting.";
    }
}
