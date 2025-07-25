package com.jehoon.food.order.domain.entity;

import java.util.List;

import com.jehoon.food.domain.entity.AggregateRoot;
import com.jehoon.food.domain.valueobject.RestaurantId;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private final boolean active;

    public boolean isActive() {
        return active;
    }

    @Builder
    public Restaurant(RestaurantId restaurantId,
            List<Product> products,
            boolean active) {
        super.setId(restaurantId);
        this.products = products;
        this.active = active;
    }
}