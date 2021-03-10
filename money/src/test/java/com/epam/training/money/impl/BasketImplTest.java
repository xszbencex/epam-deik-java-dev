package com.epam.training.money.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BasketImplTest {

    public static final double PRODUCT_VALUE = 12.5;

    @Test
    public void testAddProductShouldAddASingleProductToTheBasket() {
        // Given
        BasketImpl underTest = new BasketImpl();
        Product productToAdd = Mockito.mock(Product.class);

        // When
        underTest.addProduct(productToAdd);

        // Then
        assertThat(underTest.getProductsFromBasket(), equalTo(List.of(productToAdd)));
    }

    @Test
    public void testRemoveProductSouldRemoveSingleProductFromTheBasket() {
        // Given
        BasketImpl underTest = new BasketImpl();
        Product productToAdd = Mockito.mock(Product.class);
        underTest.addProduct(productToAdd);

        // When
        underTest.removeProduct(productToAdd);

        // Then
        assertThat(underTest.getProductsFromBasket(), equalTo(Collections.emptyList()));
    }

    @Test
    public void testAddCouponShouldAddSingleCouponToTheBasket() {
        // Given
        BasketImpl underTest = new BasketImpl();
        Coupon couponToAdd = Mockito.mock(Coupon.class);

        // When
        underTest.addCoupon(couponToAdd);

        // Then
        assertThat(underTest.getCouponsFromBasket(), equalTo(List.of(couponToAdd)));
    }

    @Test
    public void testGetValuesFromBasketShouldReturnTheBasketValueWhenASingleProductIsInTheBasket() {
        // Given
        BasketImpl underTest = new BasketImpl();
        Product product = Mockito.mock(Product.class);
        underTest.addProduct(product);
        given(product.getValue()).willReturn(PRODUCT_VALUE);

        // When
        double result = underTest.getValuesFromBasket();

        // Then
        assertThat(result, equalTo(PRODUCT_VALUE));

    }
}