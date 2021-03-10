package com.epam.training.money.impl;

import java.util.Currency;

public class Money {

    private final double value;
    private final Currency currency;

    public Money(double value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money moneyToAdd, CurrencyConversionService currencyConversionService) {
        moneyToAdd = currencyConversionService.convert(moneyToAdd, this.currency);
        return new Money(this.value + moneyToAdd.getValue(), this.currency);
    }

    public Integer compareTo(Money moneyToCompare, CurrencyConversionService currencyConversionService) {
        moneyToCompare = currencyConversionService.convert(moneyToCompare, this.currency);
        return Double.compare(this.getValue(), moneyToCompare.getValue());
    }
}