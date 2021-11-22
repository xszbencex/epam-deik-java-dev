package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.model.config.ScreeningId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ScreeningId.class)
public class Screening {

    @Id
    private String movieName;

    @Id
    private String roomName;

    @Id
    private LocalDateTime startingAt;
}
