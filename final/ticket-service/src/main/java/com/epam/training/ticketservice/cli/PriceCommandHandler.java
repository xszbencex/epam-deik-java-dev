package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.PriceService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;

@ShellComponent
public class PriceCommandHandler {

    private final PriceService priceService;
    private final AccountService accountService;

    public PriceCommandHandler(PriceService priceService, AccountService accountService) {
        this.priceService = priceService;
        this.accountService = accountService;
    }

    @ShellMethod(value = "Update the value of base price", key = {"update base price", "ubp"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public void updateBasePrice(Integer basePrice) {
        priceService.setBasePrice(basePrice);
    }

    @ShellMethod(value = "Get the value of base price", key = {"get base price", "gbp"})
    public String getBasePrice() {
        return String.valueOf(this.priceService.getBasePrice());
    }

    @ShellMethod(value = "Create price component", key = {"create price component", "cpc"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createPriceComponent(String name, Integer amount) {
        this.priceService.createPriceComponent(new PriceComponent(name, amount));
        return String.format("Price component with name '%s' successfully created.", name);
    }

    @ShellMethod(value = "Attach price component to a room", key = {"attach price component to room", "apctr"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String attachPriceComponentToRoom(String priceComponentName, String roomName) {
        try {
            this.priceService.attachPriceComponentToRoom(priceComponentName, roomName);
            return String.format("Price component with name '%s' successfully attached to room '%s'.",
                    priceComponentName, roomName);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Attach price component to a movie", key = {"attach price component to movie", "apctm"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String attachPriceComponentToMovie(String priceComponentName, String movieName) {
        try {
            this.priceService.attachPriceComponentToMovie(priceComponentName, movieName);
            return String.format("Price component with name '%s' successfully attached to movie '%s'.",
                    priceComponentName, movieName);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Attach price component to a screening",
            key = {"attach price component to screening", "apcts"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String attachPriceComponentToScreening(String priceComponentName,
                                                  String movieName,
                                                  String roomName,
                                                  String startingAt) {
        try {
            this.priceService.attachPriceComponentToScreening(priceComponentName, movieName, roomName, startingAt);
            return String.format("Price component with name '%s' successfully attached to the screening.",
                    priceComponentName);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Shows price for a booking", key = {"show price for", "spf"})
    public String showPriceOfBooking(String movieName,
                                     String roomName,
                                     String startingAt,
                                     String seats) {
        return String.format("The price for this booking would be %s HUF",
                priceService.calculatePriceForBooking(movieName, roomName, startingAt, seats.split(" ").length));
    }

    public Availability checkAdminAvailability() {
        return this.accountService.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
