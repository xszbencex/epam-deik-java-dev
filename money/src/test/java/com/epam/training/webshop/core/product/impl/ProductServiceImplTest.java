package com.epam.training.webshop.core.product.impl;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class ProductServiceImplTest {

    private ProductServiceImpl underTest;

    @BeforeEach
    public void init() {
        underTest = new ProductServiceImpl();
        underTest.init();
    }

    @Test
    public void testGetProductListShouldReturnAStaticListWithTwoElement() {
        // Given

        // When
        List<Product> actual = underTest.getProductList();

        // Then
        Assertions.assertTrue(actual.size() == 2);
    }

    @Test
    public void testGetProductByNameShouldReturnTVWhenInputProductNameIsTV() {
        // Given
        Product expected = new Product.Builder()
                .withName("TV")
                .withNetPrice(new Money(100_000D, Currency.getInstance("HUF")))
                .build();

        // When
        Optional<Product> actual = underTest.getProductByName("TV");

        // Then
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    public void testGetProductByNameShouldReturnMobilWhenInputProductNameIsTV() {
        // Given
        Product expected = new Product.Builder()
                .withName("Mobil")
                .withNetPrice(new Money(300_000D, Currency.getInstance("HUF")))
                .build();

        // When
        Optional<Product> actual = underTest.getProductByName("Mobil");

        // Then
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    public void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameDoesNotExist() {
        // Given
        Optional<Product> expected = Optional.empty();

        // When
        Optional<Product> actual = underTest.getProductByName("Notebook");

        // Then
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameIsNull() {
        // Given
        Optional<Product> expected = Optional.empty();

        // When
        Optional<Product> actual = underTest.getProductByName(null);

        // Then
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
    }

}
