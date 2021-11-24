package com.epam.training.ticketservice.model.config;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Embeddable
public class ScreeningId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "movie_fk")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_fk")
    private Room room;

    @Column(name = "starting_at")
    private LocalDateTime startingAt;
}
