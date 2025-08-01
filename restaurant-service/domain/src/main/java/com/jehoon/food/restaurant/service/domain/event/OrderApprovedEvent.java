package com.jehoon.food.restaurant.service.domain.event;

import java.time.ZonedDateTime;
import java.util.List;

import com.jehoon.food.domain.event.DomainEvent;
import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.restaurant.service.domain.Product;
import com.jehoon.food.restaurant.service.domain.Restaurant;

import lombok.Getter;

@Getter
public class OrderApprovedEvent implements DomainEvent<Restaurant> {
    private final Restaurant restaurant;
    private final OrderId orderId;
    private final List<Product> products;
    private final ZonedDateTime createdAt;

    public OrderApprovedEvent(Restaurant restaurant, OrderId orderId, List<Product> products, ZonedDateTime createdAt) {
        this.restaurant = restaurant;
        this.orderId = orderId;
        this.products = products;
        this.createdAt = createdAt;
    }
}