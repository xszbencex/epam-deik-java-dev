package com.epam.training.webshop.core.product;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.Product;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private List<Product> productList;

    public void init() {
        productList = List.of(
                new Product.Builder()
                        .withName("TV")
                        .withNetPrice(new Money(100_000D, Currency.getInstance("HUF")))
                        .build(),
                new Product.Builder()
                        .withName("Mobil")
                        .withNetPrice(new Money(300_000D, Currency.getInstance("HUF")))
                        .build());
    }

    @Override
    public List<Product> getProductList() {
        return productList;
    }

    @Override
    public Optional<Product> getProductByName(String productName) {
        return productList.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst();
    }

}
