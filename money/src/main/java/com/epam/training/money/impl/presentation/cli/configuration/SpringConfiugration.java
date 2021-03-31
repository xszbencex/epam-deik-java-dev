package com.epam.training.money.impl.presentation.cli.configuration;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties")
})
public class SpringConfiugration {

    @Bean
    public static PlaceholderConfigurerSupport propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
