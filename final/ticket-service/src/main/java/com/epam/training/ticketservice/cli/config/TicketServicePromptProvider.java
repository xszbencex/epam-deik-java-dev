package com.epam.training.ticketservice.cli.config;

import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
class TicketServicePromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Ticket service>");
    }

}
