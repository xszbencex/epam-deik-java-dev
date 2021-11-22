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
public class Account {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    private Boolean admin;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
