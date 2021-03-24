package com.epam.training.money.impl.presentation.cli.command;

public class InvalidCommand implements Command {
    @Override
    public String execute() {
        return "Unknown command.";
    }
}
