package com.epam.training.webshop.core.finance.bank.staticbank;

import com.epam.training.webshop.core.finance.bank.staticbank.model.StaticExchangeRates;

import java.util.function.Supplier;

@FunctionalInterface
public interface StaticExchangeRateSupplier extends Supplier<StaticExchangeRates> {

}
