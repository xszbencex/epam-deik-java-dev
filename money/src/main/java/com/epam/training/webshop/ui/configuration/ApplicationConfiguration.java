package com.epam.training.webshop.ui.configuration;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.cart.grossprice.impl.GrossPriceCalculatorImpl;
import com.epam.training.webshop.core.cart.grossprice.impl.HungarianTaxGrossPriceCalculator;
import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.bank.staticbank.impl.StaticBank;
import com.epam.training.webshop.core.finance.bank.staticbank.model.StaticExchangeRates;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.impl.ProductServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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