package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.OrderService;
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
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService, CheckoutObserver {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final LoginService loginService;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, LoginService loginService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.loginService = loginService;
    }

    @Override
    public void createOrder(OrderDto orderDto, UserDto userDto) {
        Objects.requireNonNull(orderDto, "OrderDto cannot be null");
        Objects.requireNonNull(userDto, "UserDto cannot be null");
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("The given user does not exist"));
        List<OrderItem> orderItemList = createOrderItemList(orderDto.getProductList());
        Order order = new Order(
                null,
                orderItemList,
                user,
                orderDto.getNetPrice().getAmount(),
                orderDto.getNetPrice().getCurrency().getCurrencyCode(),
                orderDto.getGrossPrice().getAmount(),
                orderDto.getGrossPrice().getCurrency().getCurrencyCode()
        );
        orderRepository.save(order);
    }

    @Override
    public List<OrderDto> retrieveOrdersForUser(UserDto userDto) {
        Objects.requireNonNull(userDto, "UserDto cannot be null");
        return orderRepository.findByUserUsername(userDto.getUsername()).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void handleOrder(OrderDto orderDto) {
        Objects.requireNonNull(orderDto, "OrderDto cannot be null");
        UserDto userDto = loginService.getLoggedInUser()
                .orElseThrow(() -> new IllegalArgumentException("There is no logged in user"));
        createOrder(orderDto, userDto);
    }

    private OrderDto convertEntityToDto(Order order) {
        return new OrderDto(
                order.getOrderItemList().stream().map(this::convertOrderItemToProduct).collect(Collectors.toList()),
                new Money(order.getNetPriceAmount(), Currency.getInstance(order.getNetPriceCurrencyCode())),
                new Money(order.getGrossPriceAmount(), Currency.getInstance(order.getGrossPriceCurrencyCode()))
        );
    }

    private List<OrderItem> createOrderItemList(List<ProductDto> productList) {
        return productList.stream().map(this::convertProductToOrderItem).collect(Collectors.toList());
    }

    private ProductDto convertOrderItemToProduct(OrderItem orderItem) {
        return new ProductDto.Builder()
                .withName(orderItem.getName())
                .withNetPrice(new Money(orderItem.getNetPriceAmount(), Currency.getInstance(orderItem.getNetPriceCurrencyCode())))
                .build();

    }

    private OrderItem convertProductToOrderItem(ProductDto productDto) {
        return new OrderItem(
                null,
                productDto.getName(),
                productDto.getNetPrice().getAmount(),
                productDto.getNetPrice().getCurrency().getCurrencyCode());
    }

}
