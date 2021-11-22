package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.repositry.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount() {

    }
}
