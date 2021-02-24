package com.epam.training.money.impl;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Money {

    private static final Currency USD_CURRENCY = Currency.getInstance("USD");
    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");
    private final double value;
    private final Currency currency;
    private final List<ConversionRate> conversionRates = new ArrayList<>();

    public Money(double value, Currency currency) {
        this.value = value;
        this.currency = currency;
        this.conversionRates.add(new ConversionRate(HUF_CURRENCY, USD_CURRENCY, 249.3));
        this.conversionRates.add(new ConversionRate(USD_CURRENCY, HUF_CURRENCY, 0.0034));
    }

    public double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money moneyToAdd) {
        moneyToAdd = convert(moneyToAdd);
        return new Money(this.value+moneyToAdd.getValue(),this.currency);
    }

    public Integer compareTo(Money moneyToCompare) {
        moneyToCompare = convert(moneyToCompare);
        return Double.compare(this.getValue(), moneyToCompare.getValue());
    }

    private Money convert(Money moneyToConvert) {
//        if (!isCurrencyMatches(moneyToConvert)) {
//            if (USD_CURRENCY.equals(getCurrency())
//                    && HUF_CURRENCY.equals(moneyToConvert.getCurrency()))
//                moneyToConvert = new Money(moneyToConvert.value * 0.0034, USD_CURRENCY);
//            else if (HUF_CURRENCY.equals(getCurrency())
//                    && USD_CURRENCY.equals(moneyToConvert.getCurrency()))
//                moneyToConvert = new Money(moneyToConvert.value * 249.3, HUF_CURRENCY);
//            else throw new UnsupportedOperationException("Unknown currency");
//        }
        for (int i = 0; i < conversionRates.size(); i++) {
            if (moneyToConvert.currency.equals(conversionRates.get(i).source)
                    && this.currency.equals(conversionRates.get(i).target)) {
                return new Money(conversionRates.get(i).convert(moneyToConvert.value), this.currency);
            }
        }
        throw new UnsupportedOperationException("Unknown currency");
    }

    private boolean isCurrencyMatches(Money moneyToConvert) {
        return this.currency.equals(moneyToConvert.getCurrency());
    }

    private class ConversionRate {
        private final Currency source;
        private final Currency target;
        private final double rate;

        public ConversionRate(Currency source, Currency target, double rate) {
            this.source = source;
            this.target = target;
            this.rate = rate;
        }

        public double convert(double value) {
            return value * this.rate;
        }
    }
}