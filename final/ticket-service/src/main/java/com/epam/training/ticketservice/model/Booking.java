package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private Integer reservedSeatRow;

    @Column(nullable = false)
    private Integer reservedSeatColumn;

    @ManyToOne
    @JoinColumn(name = "account_fk", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Long price;
}
