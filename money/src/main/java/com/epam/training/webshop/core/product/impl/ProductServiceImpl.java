package com.epam.training.webshop.core.product.impl;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.Product;

import java.util.*;

public class ProductServiceImpl implements ProductService {

    private List<Product> productList;

    public void init() {
        productList = new LinkedList<>(List.of(
                new Product.Builder()
                        .withName("TV")
                        .withNetPrice(new Money(100_000D, Currency.getInstance("HUF")))
                        .build(),
                new Product.Builder()
                        .withName("Mobil")
                        .withNetPrice(new Money(300_000D, Currency.getInstance("HUF")))
                        .build()));
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

    @Override
    public void createProduct(Product product) {
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(product.getName(), "Product Name cannot be null");
        Objects.requireNonNull(product.getNetPrice(), "Product Net Price cannot be null");
        productList.add(product);
    }

}
