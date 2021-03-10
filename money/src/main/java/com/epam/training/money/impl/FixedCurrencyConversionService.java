package com.epam.training.money.impl;

import java.util.Currency;
import java.util.List;

public class FixedCurrencyConversionService implements CurrencyConversionService {
    private static final Currency USD_CURRENCY = Currency.getInstance("USD");
    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");

    private static final List<ConversionRate> CONVERSION_RATES = List.of(
            new ConversionRate(HUF_CURRENCY, USD_CURRENCY, 0.0034),
            new ConversionRate(USD_CURRENCY, HUF_CURRENCY, 249.3));

    @Override
    public Money convert(Money moneyToConvert, Currency targetCurrency) {
        if (targetCurrency.equals(moneyToConvert.getCurrency())) {
            return moneyToConvert;
        }
        return CONVERSION_RATES.stream()
                .filter(currentConversionRate -> moneyToConvert.getCurrency().equals(currentConversionRate.source)
                        && targetCurrency.equals(currentConversionRate.target))
                .map(currentConversionRate -> new Money(currentConversionRate.convert(moneyToConvert.getValue()), targetCurrency))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Unknown currency"));
    }

    private static class ConversionRate {
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
