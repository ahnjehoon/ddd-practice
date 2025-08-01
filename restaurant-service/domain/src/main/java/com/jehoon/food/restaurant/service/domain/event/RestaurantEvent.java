package com.jehoon.food.restaurant.service.domain.event;

import java.time.ZonedDateTime;

import com.jehoon.food.domain.event.DomainEvent;
import com.jehoon.food.restaurant.service.domain.Restaurant;

import lombok.Getter;

@Getter
public abstract class RestaurantEvent implements DomainEvent<Restaurant> {
    private final Restaurant restaurant;
    private final ZonedDateTime createdAt;

    public RestaurantEvent(Restaurant restaurant, ZonedDateTime createdAt) {
        this.restaurant = restaurant;
        this.createdAt = createdAt;
    }
}