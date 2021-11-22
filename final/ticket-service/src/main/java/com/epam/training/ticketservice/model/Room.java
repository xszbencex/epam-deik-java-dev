package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {

    @Id
    private String name;

    @Column(nullable = false)
    private Integer rowCount;

    @Column(nullable = false)
    private Integer columnCount;
}
