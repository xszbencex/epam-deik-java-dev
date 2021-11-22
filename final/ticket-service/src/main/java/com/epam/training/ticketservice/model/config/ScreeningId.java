package com.epam.training.ticketservice.model.config;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ScreeningId implements Serializable {

    private String movieName;

    private String roomName;

    private String startingAt;
}
