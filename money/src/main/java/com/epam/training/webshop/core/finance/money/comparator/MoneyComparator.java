package com.epam.training.webshop.core.finance.money.comparator;

import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.money.Money;

import java.util.Comparator;

public class MoneyComparator implements Comparator<Money> {

    private final Bank bank;

    public MoneyComparator(Bank bank) {
        this.bank = bank;
    }

    @Override
    public int compare(Money o1, Money o2) {
        return Double.compare(o1.getAmount(), o2.to(o1.getCurrency(), bank).getAmount());
    }
}
