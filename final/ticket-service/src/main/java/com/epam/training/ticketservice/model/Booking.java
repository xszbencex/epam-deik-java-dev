package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Screening screening;

    @Column(nullable = false)
    private Integer reservedRow;

    @Column(nullable = false)
    private Integer reservedColumn;
}
