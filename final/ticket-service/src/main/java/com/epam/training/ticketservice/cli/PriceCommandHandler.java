package com.epam.training.ticketservice.cli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ConfigurationProperties(prefix = "ticket-service")
public class PriceCommandHandler {

    @Value("${price.base}")
    private int basePrice;

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    @ShellMethod(value = "Update the value of base price", key = "update base price")
    public void updateBasePrice(Integer basePrice) {
        setBasePrice(basePrice);
    }
}
