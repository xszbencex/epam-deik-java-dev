package com.epam.training.webshop.ui.configuration;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.cart.grossprice.impl.GrossPriceCalculatorImpl;
import com.epam.training.webshop.core.cart.grossprice.impl.HungarianTaxGrossPriceCalculator;
import com.epam.training.webshop.core.checkout.CheckoutService;
import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.bank.staticbank.impl.StaticBank;
import com.epam.training.webshop.core.finance.bank.staticbank.model.StaticExchangeRates;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.impl.ProductServiceImpl;
import com.epam.training.webshop.ui.command.impl.AbstractCommand;
import com.epam.training.webshop.ui.command.impl.UserAddProductToCartCommand;
import com.epam.training.webshop.ui.command.impl.UserCheckoutCartCommand;
import com.epam.training.webshop.ui.command.impl.UserProductListCommand;
import com.epam.training.webshop.ui.interpreter.CommandLineInterpreter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ApplicationConfiguration {

    @Bean(initMethod = "init")
    public ProductService productService() {
        return new ProductServiceImpl();
    }

    @Bean
    public GrossPriceCalculator grossPriceCalculator() {
        return new HungarianTaxGrossPriceCalculator(new GrossPriceCalculatorImpl());
    }

    @Bean
    public AbstractCommand userProductListCommand(ProductService productService) {
        return new UserProductListCommand(productService);
    }

    @Bean
    public AbstractCommand userAddProductToCartCommand(ProductService productService, Cart cart) {
        return new UserAddProductToCartCommand(productService, cart);
    }

    @Bean
    public AbstractCommand userCheckoutCartCommand(CheckoutService checkoutService, Cart cart) {
        return new UserCheckoutCartCommand(checkoutService, cart);
    }

    @Bean
    public CommandLineInterpreter commandLineInterpreter(Set<AbstractCommand> commands) {
        return new CommandLineInterpreter(System.in, System.out, commands);
    }

    @Bean
    public Cart cart(Bank bank){
        return  new Cart(bank);
    }

    @Bean
    public Bank bank(){
        return StaticBank.of(() -> new StaticExchangeRates.Builder()
                .addRate("HUF", "USD", 0.0034, 249.3)
                .build());
    }

}