package com.jehoon.food.common.messaging.kafka.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestModel {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("sagaId")
    private String sagaId;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("createdAt")
    private Instant createdAt;
    
    @JsonProperty("paymentOrderStatus")
    private PaymentOrderStatus paymentOrderStatus;
    
    public enum PaymentOrderStatus {
        PENDING, CANCELLED
    }
}