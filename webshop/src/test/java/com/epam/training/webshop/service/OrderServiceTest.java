package com.epam.training.webshop.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.training.webshop.domain.order.Basket;
import com.epam.training.webshop.domain.order.Order;
import com.epam.training.webshop.repository.OrderRepository;
import com.epam.training.webshop.repository.ProductRepository;

class OrderServiceTest {

    private OrderService underTest;

    @Mock
    private Basket basket;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Order order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new OrderService(basket, productRepository, orderRepository);
    }

    @Test
    public void testOrderCurrentBasketShouldReturnCreatedOrder() {
        // Given
        given(basket.order()).willReturn(order);

        // When
        Order result = underTest.orderCurrentBasket();

        // Then
        assertThat(result, equalTo(order));
    }

}