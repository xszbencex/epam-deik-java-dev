package com.epam.training.webshop.core.cart;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;

import java.util.*;

public class Cart implements CheckoutObserver {

    private List<ProductDto> productList;
    private final Bank bank;

    public static Cart of(Bank bank, ProductDto... products) {
        return new Cart(bank, new LinkedList<ProductDto>(Arrays.asList(products.clone())));
    }

    public Cart(Bank bank) {
        this(bank, new LinkedList<>());
    }

    Cart(Bank bank, List<ProductDto> productList) {
        this.bank = bank;
        this.productList = productList;
    }

    public void add(ProductDto product) {
        Objects.requireNonNull(product, "Product is a mandatory parameter for adding something to the cart");
        productList.add(product);
    }

    public Money getAggregatedNetPrice() {
        Money aggregatedPrice = new Money(0D, Currency.getInstance("HUF"));
        for (ProductDto product : productList) {
            aggregatedPrice = aggregatedPrice.add(product.getNetPrice(), bank);
        }
        return aggregatedPrice;
    }

    public List<ProductDto> getProductList() {
        return Collections.unmodifiableList(productList);
    }

    @Override
    public void handleOrder(OrderDto orderDto) {
        productList = new LinkedList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(productList, cart.productList) && Objects.equals(bank, cart.bank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productList, bank);
    }

}
