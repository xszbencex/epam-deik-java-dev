package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.exception.UsernameTakenException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class AccountCommandHandler {

    private final AccountService accountService;

    public AccountCommandHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @ShellMethod(value = "Sign up an account", key = {"sign up", "su"})
    public String signUp(final String username, final String password) {
        try {
            this.accountService.createAccount(new Account(username, password));
            return String.format("Successfully signed up as '%s'", username);
        } catch (UsernameTakenException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Sing in with credentials", key = {"sign in", "si"})
    public String signIn(final String username, final String password) {
        final Optional<Account> account = this.accountService.getAccountById(username);
        if (account.filter(acc -> !acc.getAdmin()).isPresent() && password.equals(account.get().getPassword())) {
            this.accountService.signIn(account.get());
            return "Successfully signed in";
        } else if (account.filter(Account::getAdmin).isPresent()) {
            return String.format("'%s' is a privileged user. "
                    + "Privileged users must log in with the 'sign up privileged' command.", username);
        } else {
            return "Login failed due to incorrect credentials";
        }
    }

    @ShellMethod(value = "Sign in with credentials to admin account", key = {"sign in privileged", "sip"})
    public String signInPrivileged(final String username, final String password) {
        final Optional<Account> account = this.accountService.getAccountById(username);
        if (account.filter(Account::getAdmin).isPresent() && password.equals(account.get().getPassword())) {
            this.accountService.signIn(account.get());
            return "Successfully signed in";
        } else if (account.filter(acc -> !acc.getAdmin()).isPresent()) {
            return String.format("'%s' is not a privileged user", username);
        } else {
            return "Login failed due to incorrect credentials";
        }
    }

    @ShellMethod(value = "Sign out from account", key = {"sign out", "so"})
    @ShellMethodAvailability(value = "checkLoggedInAvailability")
    public String signOut() {
        this.accountService.signOut();
        return "Successfully signed out";
    }

    @ShellMethod(value = "Query signed in account info", key = {"describe account", "da"})
    public String describeAccount() {
        final Optional<Account> loggedInAccount = this.accountService.getLoggedInAccount();
        if (loggedInAccount.isPresent()) {
            return this.accountService.formattedAccountDescription(loggedInAccount.get());
        } else {
            return "You are not signed in";
        }
    }

    public Availability checkLoggedInAvailability() {
        return this.accountService.getLoggedInAccount().isPresent()
                ? Availability.available()
                : Availability.unavailable("you are not signed in.");
    }
}
