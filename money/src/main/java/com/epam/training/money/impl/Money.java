package com.epam.training.money.impl;

import java.util.Currency;

public class Money {

    private final double amount;
    private final Currency currency;

    public Money(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money moneyToAdd, Bank bank) {
        Money money = convert(moneyToAdd, bank);
        return new Money(this.amount + money.getAmount(), this.currency);
    }

    public Integer compareTo(Money moneyToCompare, Bank bank) {
        Money money = convert(moneyToCompare, bank);
        return Double.compare(this.getAmount(), money.getAmount());
    }

    private Money convert(Money other, Bank bank){
        if (!isInTheSameCurrency(other)) {
            Double exchangeRate = bank.getExchangeRate(this.getCurrency(),other.getCurrency()).orElseThrow(() -> new UnsupportedOperationException());
            return new Money(other.amount * exchangeRate, other.getCurrency());
        } else {
            return other;
        }
    }

    private boolean isInTheSameCurrency(Money money) {
        return this.currency.equals(money.getCurrency());
    }
}
