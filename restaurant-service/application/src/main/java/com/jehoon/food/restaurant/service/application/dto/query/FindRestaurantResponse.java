package com.jehoon.food.restaurant.service.application.dto.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindRestaurantResponse {
    private final UUID restaurantId;
    private final String name;
    private final Boolean active;
    private final List<ProductDto> products;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProductDto {
        private final UUID productId;
        private final String name;
        private final BigDecimal price;
        private final Integer quantity;
        private final Boolean available;
    }
}