package com.epam.training.webshop.domain.orderconfirm.lib;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.webshop.domain.order.Product;

public class EmailConfirmationService implements ConfirmationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailConfirmationService.class);

    @Override
    public void sendConfirmationMessageAbout(List<Product> products) {
        LOGGER.info("Sending an e-mail confirmation about {} products", products);
    }
}
