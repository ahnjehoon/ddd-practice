package com.jehoon.food.order.application.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.jehoon.food.domain.valueobject.OrderApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponse {
    private UUID id;
    private UUID sagaId;
    private UUID orderId;
    private UUID restaurantId;
    private Instant createdAt;
    private OrderApprovalStatus orderApprovalStatus;
    private List<String> failureMessages;
}
