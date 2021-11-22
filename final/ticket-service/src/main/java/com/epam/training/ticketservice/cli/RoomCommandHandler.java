package com.epam.training.ticketservice.cli;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class RoomCommandHandler {

    @ShellMethod(value = "Add a room to database", key = "create room")
    public String createRoom(final String name, final String rowNumber, final int columnNumber) {
        return "";
    }

    @ShellMethod(value = "Update a room in the database", key = "update room")
    public String updateRoom(final String name, final String rowNumber, final int columnNumber) {
        return "";
    }

    @ShellMethod(value = "Delete a room from the database", key = "delete room")
    public String deleteRoom(final String name) {
        return "";
    }

    @ShellMethod(value = "List the rooms", key = "list rooms")
    public String listRooms() {
        if (false) {
            return "There are no rooms at the moment";
        } else {
            return "";
        }
    }
}
