package com.jehoon.food.restaurant.service.domain.event;

import java.time.ZonedDateTime;

import com.jehoon.food.domain.event.DomainEvent;
import com.jehoon.food.restaurant.service.domain.Product;

import lombok.Getter;

@Getter
public class ProductUpdatedEvent implements DomainEvent<Product> {
    private final Product product;
    private final ZonedDateTime createdAt;

    public ProductUpdatedEvent(Product product, ZonedDateTime createdAt) {
        this.product = product;
        this.createdAt = createdAt;
    }
}