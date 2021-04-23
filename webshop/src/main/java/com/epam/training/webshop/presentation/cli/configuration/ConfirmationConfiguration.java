package com.epam.training.webshop.presentation.cli.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.training.webshop.domain.order.Observable;
import com.epam.training.webshop.domain.orderconfirm.impl.EmailConfirmationServiceAdapter;
import com.epam.training.webshop.domain.orderconfirm.lib.EmailConfirmationService;

@Configuration
public class ConfirmationConfiguration {

    @Bean
    public EmailConfirmationService emailConfirmationService(Observable basket) {
        EmailConfirmationServiceAdapter emailConfirmationService = new EmailConfirmationServiceAdapter();
        basket.subscribe(emailConfirmationService);
        return emailConfirmationService;
    }

}
