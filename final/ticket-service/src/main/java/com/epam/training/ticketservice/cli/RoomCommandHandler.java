package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.service.RoomService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class RoomCommandHandler {

    private final AccountCommandHandler accountCommandHandler;
    private final RoomService roomService;

    public RoomCommandHandler(AccountCommandHandler accountCommandHandler, RoomService roomService) {
        this.accountCommandHandler = accountCommandHandler;
        this.roomService = roomService;
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
        List<Room> rooms = this.roomService.getAllRooms();
        if (rooms.isEmpty()) {
            return "There are no rooms at the moment";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            rooms.forEach(room -> stringBuilder.append(String.format("Room %s with %s seats, %s rows and %s columns\n",
                    room.getName(),
                    room.getRowCount() * room.getColumnCount(),
                    room.getRowCount(),
                    room.getColumnCount())));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
    }

    public Availability checkAdminAvailability() {
        return this.accountCommandHandler.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
