package com.epam.training.ticketservice.repositry.init;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.repositry.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class AccountInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountInitializer.class);
    private static final Account ADMIN_ACCOUNT = new Account("admin", "admin", true);

    private final AccountRepository accountRepository;
    private final Environment environment;

    public AccountInitializer(final AccountRepository accountRepository, final Environment environment) {
        this.accountRepository = accountRepository;
        this.environment = environment;
    }

    @PostConstruct
    public void initProducts() {
        if (this.isProfileCiActive() || accountRepository.findById("admin").isEmpty()) {
            LOGGER.info("Init admin account...");
            accountRepository.save(ADMIN_ACCOUNT);
            LOGGER.info("Account initialization is finished");
        }
    }

    private boolean isProfileCiActive() {
        return Arrays.asList(this.environment.getActiveProfiles()).contains("ci");
    }

}
