package com.jehoon.food.restaurant.service.application.dto.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateRestaurantCommand {
    @NotNull
    private final UUID restaurantId;
    
    @NotBlank
    private final String name;
    
    @NotNull
    private final Boolean active;
    
    @NotEmpty
    private final List<ProductDto> products;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProductDto {
        @NotNull
        private final UUID productId;
        
        @NotBlank
        private final String name;
        
        @NotNull
        private final BigDecimal price;
        
        @NotNull
        private final Integer quantity;
        
        @NotNull
        private final Boolean available;
    }
}