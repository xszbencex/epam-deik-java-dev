package com.epam.training.webshop.cart;

import com.epam.training.webshop.finance.bank.Bank;
import com.epam.training.webshop.finance.money.Money;
import com.epam.training.webshop.product.Product;

import java.util.*;

public class Cart {

    private List<Product> productList;

    public static Cart of(Product... products) {
        return new Cart(new LinkedList<Product>(Arrays.asList(products.clone())));
    }

    public Cart() {
        this.productList = new LinkedList<>();
    }

    Cart(List<Product> productList) {
        this.productList = productList;
    }

    public void add(Product product) {
        productList.add(product);
    }

    public Money getAggregatedNetPrice(Bank bank) {
        Money aggregatedPrice = new Money(0D, Currency.getInstance("HUF"));
        for (Product product : productList) {
            aggregatedPrice = aggregatedPrice.add(product.getNetPrice(), bank);
        }
        return aggregatedPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(productList, cart.productList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productList);
    }

}
