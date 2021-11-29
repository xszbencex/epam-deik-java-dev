package com.epam.training.ticketservice.model.config;

import lombok.Getter;

@Getter
public class Seat {

    private final Integer rowNum;

    private final Integer columnNum;

    public Seat(String columnAndRow) {
        final String[] parsedColumnAndRow = columnAndRow.split(",");
        this.rowNum = Integer.parseInt(parsedColumnAndRow[0]);
        this.columnNum = Integer.parseInt(parsedColumnAndRow[1]);
    }

    @Override
    public String toString() {
        return rowNum + "," + columnNum;
    }
}
