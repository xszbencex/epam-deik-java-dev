package com.epam.training.ticketservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PriceComponent {

    @Id
    private String name;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "movie_fk")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_fk")
    private Room room;

    @ManyToOne
    private Screening screening;

    public PriceComponent(String name, Integer amount) {
        this.name = name;
        this.amount = amount;
    }
}
