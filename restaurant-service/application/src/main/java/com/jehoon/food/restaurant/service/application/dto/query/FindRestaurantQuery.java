package com.jehoon.food.restaurant.service.application.dto.query;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindRestaurantQuery {
    @NotNull
    private final UUID restaurantId;
}