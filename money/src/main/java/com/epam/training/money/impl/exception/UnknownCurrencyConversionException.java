package com.epam.training.money.impl.exception;

import java.util.Currency;

public class UnknownCurrencyConversionException extends RuntimeException {

    public UnknownCurrencyConversionException(Currency fromCurrency, Currency toCurrency) {
        super("Failed to convert from: " + fromCurrency + " to: " + toCurrency);
    }
}
