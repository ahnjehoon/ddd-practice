package com.jehoon.food.order.service.domain.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderAddressDto {
    @NotNull
    private final String street;
    @NotNull
    private final String postalCode;
    @NotNull
    private final String city;
}
