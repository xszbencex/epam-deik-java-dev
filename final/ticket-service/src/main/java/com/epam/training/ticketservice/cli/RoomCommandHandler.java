package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.RoomService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class RoomCommandHandler {

    private final RoomService roomService;
    private final AccountService accountService;

    public RoomCommandHandler(final RoomService roomService, final AccountService accountService) {
        this.roomService = roomService;
        this.accountService = accountService;
    }

    @ShellMethod(value = "Add a room to database", key = {"create room", "cr"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createRoom(final String name, final Integer rowCount, final Integer columnCount) {
        this.roomService.createRoom(new Room(name, rowCount, columnCount));
        return String.format("Room with name '%s' successfully created.", name);
    }

    @ShellMethod(value = "Update a room in the database", key = {"update room", "ur"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String updateRoom(final String name, final Integer rowCount, final Integer columnCount) {
        try {
            this.roomService.updateRoom(new Room(name, rowCount, columnCount));
            return String.format("Room with name '%s' successfully updated.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete a room from the database", key = {"delete room", "dr"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteRoom(final String name) {
        try {
            this.roomService.deleteRoom(name);
            return String.format("Room with name '%s' successfully deleted.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the rooms", key = {"list rooms", "lr"})
    public String listRooms() {
        final List<Room> rooms = this.roomService.getAllRooms();

        if (rooms.isEmpty()) {
            return "There are no rooms at the moment";
        } else {
            return roomService.formattedRoomList(rooms);
        }
    }

    public Availability checkAdminAvailability() {
        return this.accountService.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
