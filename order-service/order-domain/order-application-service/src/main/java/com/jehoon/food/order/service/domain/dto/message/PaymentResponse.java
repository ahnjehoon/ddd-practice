package com.jehoon.food.order.service.domain.dto.message;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.jehoon.food.domain.valueobject.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponse {
    private UUID id;
    private UUID sagaId;
    private UUID orderId;
    private UUID paymentId;
    private UUID customerId;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentStatus paymentStatus;
    private List<String> failureMessages;
}
