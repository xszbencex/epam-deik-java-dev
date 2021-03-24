package com.epam.training.money.impl.domain.order;

import java.util.List;

public interface Coupon {

    String getId();

    double getDiscountForProducts(List<Product> products);
}
