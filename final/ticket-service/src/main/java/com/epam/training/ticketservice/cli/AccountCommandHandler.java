package com.epam.training.ticketservice.cli;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AccountCommandHandler {

    @ShellMethod(value = "Sign up an account", key = "sign up")
    public String signUp(final String username, final String password) {
        return "";
    }

    @ShellMethod(value = "Sing in with credentials", key = "sign in")
    public String signIn(final String username, final String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return "Successfully signed in!";
        } else {
            return "Login failed due to incorrect credentials";
        }
    }

    @ShellMethod(value = "Sign in with credentials to admin account", key = "sign in privileged")
    public String signInPrivileged(final String username, final String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return "Successfully signed in!";
        } else {
            return "Login failed due to incorrect credentials";
        }
    }

    @ShellMethod(value = "Sign out from account", key = "sign out")
    public String signOut() {
        return "Successfully signed out!";
    }

    @ShellMethod(value = "Query signed in account info", key = "describe account")
    public String describeAccount() {
        if (true) {
            return "You are not signed in";
        } else {
            // TODO not privileged
            return "Signed in with privileged account '<felhasználónév>'";
        }
    }

}
