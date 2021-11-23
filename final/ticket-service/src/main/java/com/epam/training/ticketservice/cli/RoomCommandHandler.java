package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class RoomCommandHandler {

    private final AccountCommandHandler accountCommandHandler;

    public RoomCommandHandler(AccountCommandHandler accountCommandHandler) {
        this.accountCommandHandler = accountCommandHandler;
    }

    @ShellMethod(value = "Add a room to database", key = {"create room", "cr"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createRoom(final String name, final String rowNumber, final int columnNumber) {
        return "";
    }

    @ShellMethod(value = "Update a room in the database", key = {"update room", "ur"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String updateRoom(final String name, final String rowNumber, final int columnNumber) {
        return "";
    }

    @ShellMethod(value = "Delete a room from the database", key = {"delete room", "dr"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteRoom(final String name) {
        return "";
    }

    @ShellMethod(value = "List the rooms", key = {"list rooms", "lr"})
    public String listRooms() {
        if (false) {
            return "There are no rooms at the moment";
        } else {
            return "";
        }
    }

    public Availability checkAdminAvailability() {
        return this.accountCommandHandler.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
