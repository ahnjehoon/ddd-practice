package com.jehoon.food.restaurant.service.domain;

import java.util.List;

import com.jehoon.food.domain.entity.AggregateRoot;
import com.jehoon.food.domain.valueobject.RestaurantId;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final String name;
    private final boolean active;
    private final List<Product> products;

    private Restaurant(Builder builder) {
        setId(builder.restaurantId);
        name = builder.name;
        active = builder.active;
        products = builder.products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private String name;
        private boolean active;
        private List<Product> products;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
