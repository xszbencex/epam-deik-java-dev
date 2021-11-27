package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.model.config.Seat;
import com.epam.training.ticketservice.model.config.SeatListConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="moive_fk", referencedColumnName="movie_fk", nullable = false),
            @JoinColumn(name="room_fk", referencedColumnName="room_fk", nullable = false),
            @JoinColumn(name="starting_at", referencedColumnName="starting_at", nullable = false)
    })
    private Screening screening;

    @Convert(converter = SeatListConverter.class)
    private List<Seat> seats;

    @ManyToOne
    @JoinColumn(name = "account_fk", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Long price;

    public Booking(Screening screening, List<Seat> seats, Account account, Long price) {
        this.screening = screening;
        this.seats = seats;
        this.account = account;
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        seats.forEach(seat -> stringBuilder.append("(").append(seat).append(")").append(", "));
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return String.format("Seats %s on %s in room %s starting at %s for %s HUF",
                stringBuilder,
                getScreening().getId().getMovie().getName(),
                getScreening().getId().getRoom().getName(),
                getScreening().getId().getStartingAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                getPrice());
    }
}
