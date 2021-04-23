package com.epam.training.webshop.repository;

import com.epam.training.webshop.domain.order.Coupon;

public interface CouponRepository {
    Coupon getCouponWithName(String name);
}
