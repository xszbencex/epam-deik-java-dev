package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

public class CheckoutObservableTest {

    private CheckoutObservable underTest;

    @Test
    public void testBroadcastOrderShouldCallAllObserverWhenOrderIsPassed() {
        // Given
        Order order = Mockito.mock(Order.class);
        CheckoutObserver observer1 = Mockito.mock(CheckoutObserver.class);
        CheckoutObserver observer2 = Mockito.mock(CheckoutObserver.class);
        underTest = new CheckoutObservable(List.of(observer1, observer2));

        // When
        underTest.broadcastOrder(order);

        // Then
        Mockito.verify(observer1).handleOrder(order);
        Mockito.verify(observer2).handleOrder(order);
        Mockito.verifyNoMoreInteractions(observer1, observer2);
    }

}
