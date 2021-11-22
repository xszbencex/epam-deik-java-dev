package com.epam.training.ticketservice.cli;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ScreeningCommandHandler {

    @ShellMethod(value = "Add a screening to database", key = "create screening")
    public String createScreening(final String name, final String rowNumber, final int columnNumber) {
        return "";
    }

    @ShellMethod(value = "Delete a screening from the database", key = "delete screening")
    public String deleteScreening(final String name) {
        return "";
    }

    @ShellMethod(value = "List the screenings", key = "list screenings")
    public String listScreenings() {
        if (false) {
            return "There are no screenings at the moment";
        } else {
            return "";
        }
    }
}
