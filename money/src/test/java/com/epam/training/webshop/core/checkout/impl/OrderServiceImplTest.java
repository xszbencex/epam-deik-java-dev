package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.checkout.persistence.entity.Order;
import com.epam.training.webshop.core.checkout.persistence.entity.OrderItem;
import com.epam.training.webshop.core.checkout.persistence.repository.OrderRepository;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.user.LoginService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.entity.User;
import com.epam.training.webshop.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class OrderServiceImplTest {

    private OrderServiceImpl underTest;

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private LoginService loginService;

    @BeforeEach
    public void init() {
        orderRepository = Mockito.mock(OrderRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        loginService = Mockito.mock(LoginService.class);
        underTest = new OrderServiceImpl(orderRepository, userRepository, loginService);
    }

    @Test
    public void testCreateOrderShouldThrowNullPointerExceptionWhenOrderDtoIsNull() {
        // Given
        UserDto userDto = Mockito.mock(UserDto.class);

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createOrder(null, userDto));

        // Then
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService, userDto);
    }

    @Test
    public void testCreateOrderShouldThrowNullPointerExceptionWhenUserDtoIsNull() {
        // Given
        OrderDto orderDto = Mockito.mock(OrderDto.class);

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createOrder(orderDto, null));

        // Then
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService, orderDto);
    }

    @Test
    public void testCreateOrderShouldThrowIllegalArgumentExceptionWhenTheUserDoesNotExist() {
        // Given
        OrderDto orderDto = Mockito.mock(OrderDto.class);
        UserDto userDto = Mockito.mock(UserDto.class);
        Mockito.when(userDto.getUsername()).thenReturn("username");
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.createOrder(orderDto, userDto));

        // Then
        Mockito.verify(userDto).getUsername();
        Mockito.verify(userRepository).findByUsername("username");
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService, orderDto, userDto);
    }

    @Test
    public void testCreateOrderShouldCallOrderRepositorySaveMethodWhenTheParametersAreValid() {
        // Given
        ProductDto productDto = new ProductDto.Builder()
                .withName("TV")
                .withNetPrice(new Money(100, Currency.getInstance("HUF")))
                .build();
        Money orderNetPrice = new Money(100, Currency.getInstance("HUF"));
        Money orderGrossPrice = new Money(200, Currency.getInstance("HUF"));
        OrderDto orderDto = new OrderDto(List.of(productDto), orderNetPrice, orderGrossPrice);
        UserDto userDto = Mockito.mock(UserDto.class);
        Mockito.when(userDto.getUsername()).thenReturn("username");
        User user = Mockito.mock(User.class);
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        OrderItem orderItem = new OrderItem(null, "TV", 100, "HUF");
        Order expectedOrder = new Order(null, List.of(orderItem), user, 100, "HUF", 200, "HUF");

        // When
        underTest.createOrder(orderDto, userDto);

        // Then
        Mockito.verify(userDto).getUsername();
        Mockito.verify(userRepository).findByUsername("username");
        Mockito.verify(orderRepository).save(expectedOrder);
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService, userDto, user);
    }

    @Test
    public void testRetrieveOrdersForUserShouldThrowNullPointerExceptionWhenUserDtoIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.retrieveOrdersForUser(null));

        // Then
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService);
    }

    @Test
    public void testRetrieveOrdersForUserShouldReturnWithAListOfOrdersWhenUserDtoIsNotNull() {
        // Given
        UserDto userDto = Mockito.mock(UserDto.class);
        Mockito.when(userDto.getUsername()).thenReturn("username");
        User user = Mockito.mock(User.class);
        OrderItem orderItem = new OrderItem(null, "TV", 100, "HUF");
        Order order = new Order(10, List.of(orderItem), user, 100, "HUF", 200, "HUF");
        Mockito.when(orderRepository.findByUserUsername("username")).thenReturn(List.of(order));
        ProductDto productDto = new ProductDto.Builder()
                .withName("TV")
                .withNetPrice(new Money(100, Currency.getInstance("HUF")))
                .build();
        Money orderNetPrice = new Money(100, Currency.getInstance("HUF"));
        Money orderGrossPrice = new Money(200, Currency.getInstance("HUF"));
        OrderDto orderDto = new OrderDto(List.of(productDto), orderNetPrice, orderGrossPrice);
        List<OrderDto> expected = List.of(orderDto);

        // When
        List<OrderDto> actual = underTest.retrieveOrdersForUser(userDto);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(userDto).getUsername();
        Mockito.verify(orderRepository).findByUserUsername("username");
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService, userDto, user);
    }

    @Test
    public void testRetrieveOrdersForUserShouldReturnWithANEmptyListWhenThereIsNoOrderForTheGivenUser() {
        // Given
        UserDto userDto = Mockito.mock(UserDto.class);
        Mockito.when(userDto.getUsername()).thenReturn("username");
        Mockito.when(orderRepository.findByUserUsername("username")).thenReturn(Collections.EMPTY_LIST);
        List<OrderDto> expected = Collections.EMPTY_LIST;

        // When
        List<OrderDto> actual = underTest.retrieveOrdersForUser(userDto);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(userDto).getUsername();
        Mockito.verify(orderRepository).findByUserUsername("username");
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService, userDto);
    }

    @Test
    public void testHandleOrderShouldThrowNullPointerExceptionWhenOrderDtoIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.handleOrder(null));

        // Then
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService);
    }

    @Test
    public void testHandleOrderShouldThrowIllegalArgumentExceptionWhenThereIsNoUserLoggedIn() {
        // Given
        OrderDto orderDto = Mockito.mock(OrderDto.class);
        Mockito.when(loginService.getLoggedInUser()).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.handleOrder(orderDto));

        // Then
        Mockito.verify(loginService).getLoggedInUser();
        Mockito.verifyNoMoreInteractions(orderRepository, userRepository, loginService, orderDto);
    }

}
