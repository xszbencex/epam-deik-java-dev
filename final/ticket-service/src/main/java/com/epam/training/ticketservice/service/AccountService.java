package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.repositry.AccountRepository;
import com.epam.training.ticketservice.service.exception.UsernameTakenException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BookingService bookingService;

    public AccountService(AccountRepository accountRepository, BookingService bookingService) {
        this.accountRepository = accountRepository;
        this.bookingService = bookingService;
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

    public String formattedAccountDescription(Account account) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Signed in with%s account '%s'\n",
                account.getAdmin() ? " privileged" : "",
                account.getUsername()));
        List<Booking> accountBookings = this.bookingService.getBookingsByUsername(account.getUsername());
        if (accountBookings.isEmpty()) {
            stringBuilder.append("You have not booked any tickets yet");
        } else {
            stringBuilder.append("Your previous bookings are\n");
            accountBookings.forEach(booking -> stringBuilder.append(booking).append("\n"));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }
}
