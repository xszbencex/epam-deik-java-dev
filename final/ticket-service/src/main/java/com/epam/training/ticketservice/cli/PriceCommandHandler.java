package com.epam.training.ticketservice.cli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class PriceCommandHandler {

    private final AccountCommandHandler accountCommandHandler;

    @Value("${ticket-service.price.base}")
    private int basePrice;

    public PriceCommandHandler(AccountCommandHandler accountCommandHandler) {
        this.accountCommandHandler = accountCommandHandler;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    @ShellMethod(value = "Update the value of base price", key = {"update base price", "ubp"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public void updateBasePrice(Integer basePrice) {
        setBasePrice(basePrice);
    }

    public Availability checkAdminAvailability() {
        return this.accountCommandHandler.getLoggedInAccount().isPresent()
                && this.accountCommandHandler.getLoggedInAccount().get().getAdmin()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
