package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.service.AccountService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class AccountCommandHandler {

    private final AccountService accountService;
    private Optional<Account> loggedInAccount = Optional.empty();

    public AccountCommandHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    public Optional<Account> getLoggedInAccount() {
        return loggedInAccount;
    }

    @ShellMethod(value = "Sign up an account", key = {"sign up", "su"})
    public void signUp(final String username, final String password) {
        accountService.createAccount();
    }

    @ShellMethod(value = "Sing in with credentials", key = {"sign in", "si"})
    public String signIn(final String username, final String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            this.loggedInAccount = Optional.of(new Account(username, password));
            return "Successfully signed in!";
        } else {
            return "Login failed due to incorrect credentials";
        }
    }

    @ShellMethod(value = "Sign in with credentials to admin account", key = {"sign in privileged", "sip"})
    public String signInPrivileged(final String username, final String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            this.loggedInAccount = Optional.of(new Account("admin", "admin", true));
            return "Successfully signed in!";
        } else {
            return "Login failed due to incorrect credentials";
        }
    }

    @ShellMethod(value = "Sign out from account", key = {"sign out", "so"})
    @ShellMethodAvailability(value = "checkLoggedInAvailability")
    public String signOut() {
        this.loggedInAccount = Optional.empty();
        return "Successfully signed out!";
    }

    @ShellMethod(value = "Query signed in account info", key = {"describe account", "da"})
    @ShellMethodAvailability(value = "checkLoggedInAvailability")
    public String describeAccount() {
        return String.format("Signed in with privileged account '%s'", loggedInAccount.orElseThrow().getUsername());
    }

    public Availability checkLoggedInAvailability() {
        return this.loggedInAccount.isPresent()
                ? Availability.available()
                : Availability.unavailable("you are not signed in.");
    }
}
