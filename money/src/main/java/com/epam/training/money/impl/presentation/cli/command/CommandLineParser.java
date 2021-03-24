package com.epam.training.money.impl.presentation.cli.command;

public interface CommandLineParser {
    Command createCommand(String commandLine);
    void setSuccessor(CommandLineParser successor);
}
