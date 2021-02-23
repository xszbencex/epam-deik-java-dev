package com.epam.training.money.impl;

import java.util.*;

public class MapBank implements Bank {

    private Map<CurrencyPair,Double> exchangeRatesMap;

    public MapBank() {
        this.exchangeRatesMap = new HashMap<>();
        exchangeRatesMap.put(CurrencyPair.of("USD","HUF"), 0.0034);
        exchangeRatesMap.put(CurrencyPair.of("HUF","USD"), 249.3);
    }

    @Override
    public Optional<Double> getExchangeRate(Currency from, Currency to) {
        return Optional.ofNullable(exchangeRatesMap.get(CurrencyPair.of(from, to)));
    }

    private static class CurrencyPair {
        private final Currency from;
        private final Currency to;

        public static CurrencyPair of(Currency from, Currency to) {
            return new CurrencyPair(from, to);
        }

        public static CurrencyPair of(String from, String to) {
            return new CurrencyPair(Currency.getInstance(from), Currency.getInstance(to));
        }

        private CurrencyPair(Currency from, Currency to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CurrencyPair that = (CurrencyPair) o;
            return Objects.equals(from, that.from) && Objects.equals(to, that.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return "CurrencyPair{" +
                    "from=" + from +
                    ", to=" + to +
                    '}';
        }
    }
}
