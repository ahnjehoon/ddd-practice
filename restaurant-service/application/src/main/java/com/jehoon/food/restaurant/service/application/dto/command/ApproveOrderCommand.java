package com.jehoon.food.restaurant.service.application.dto.command;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApproveOrderCommand {
    @NotNull
    private final UUID restaurantId;
    
    @NotNull
    private final UUID orderId;
    
    @NotEmpty
    private final List<OrderProductDto> products;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderProductDto {
        @NotNull
        private final UUID productId;
        
        @NotNull
        private final String name;
        
        @NotNull
        private final Integer quantity;
    }
}