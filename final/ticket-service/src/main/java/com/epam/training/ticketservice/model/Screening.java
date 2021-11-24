package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.model.config.ScreeningId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Screening implements Serializable {

    @EmbeddedId
    private ScreeningId id;

    @Override
    public String toString() {
        return String.format("%s (%s, %s minutes), screened in room %s, at %s",
                id.getMovie().getName(), id.getMovie().getGenre(), id.getMovie().getLength(), id.getRoom().getName(),
                id.getStartingAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
