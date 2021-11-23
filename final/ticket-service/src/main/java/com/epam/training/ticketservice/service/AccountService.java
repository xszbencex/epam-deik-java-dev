package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.repositry.AccountRepository;
import com.epam.training.ticketservice.service.exception.UsernameTakenException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(Account account) throws UsernameTakenException {
        if (this.accountRepository.findById(account.getUsername()).isPresent()) {
            throw new UsernameTakenException();
        } else {
            this.accountRepository.save(account);
        }
    }

    public Optional<Account> getAccountById(final String username) {
        return this.accountRepository.findById(username);
    }
}
