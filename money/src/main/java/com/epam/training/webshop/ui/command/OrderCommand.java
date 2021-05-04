package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.checkout.OrderService;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.user.LoginService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class OrderCommand extends AbstractAuthenticatedCommand {

    private final OrderService orderService;

    public OrderCommand(LoginService loginService, OrderService orderService) {
        super(loginService);
        this.orderService = orderService;
    }

    @ShellMethodAvailability("loggedIn")
    @ShellMethod(value = "User Order List", key = "user order list")
    public List<OrderDto> listOrder() {
        return orderService.retrieveOrdersForUser(getUserDto());
    }

}
