package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.PriceService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

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

    public Availability checkAdminAvailability() {
        return this.accountService.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
