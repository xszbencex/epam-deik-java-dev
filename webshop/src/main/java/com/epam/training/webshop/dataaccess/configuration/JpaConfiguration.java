package com.epam.training.webshop.dataaccess.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.epam.training.webshop.dataaccess.dao")
@EntityScan("com.epam.training.webshop.dataaccess.projection")
@EnableTransactionManagement
public class JpaConfiguration {

}
