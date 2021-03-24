package com.epam.training.money.impl.repository;

import com.epam.training.money.impl.domain.order.Coupon;

public interface CouponRepository {
    Coupon getCouponWithName(String name);
}
