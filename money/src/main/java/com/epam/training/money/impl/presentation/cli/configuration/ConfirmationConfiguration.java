package com.epam.training.money.impl.presentation.cli.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.training.money.impl.domain.order.Observable;
import com.epam.training.money.impl.domain.orderconfirm.impl.EmailConfirmationServiceAdapter;
import com.epam.training.money.impl.domain.orderconfirm.lib.EmailConfirmationService;

@Configuration
public class ConfirmationConfiguration {

    @Bean
    public EmailConfirmationService emailConfirmationService(Observable basket) {
        EmailConfirmationServiceAdapter emailConfirmationService = new EmailConfirmationServiceAdapter();
        basket.subscribe(emailConfirmationService);
        return emailConfirmationService;
    }

}
