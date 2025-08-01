package com.jehoon.food.restaurant.service.application.dto.command;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApproveOrderResponse {
    @NotNull
    private final UUID orderId;
    
    @NotNull
    private final String message;
}