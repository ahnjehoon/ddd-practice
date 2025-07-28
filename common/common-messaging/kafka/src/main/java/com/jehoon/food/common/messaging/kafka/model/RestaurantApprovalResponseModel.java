package com.jehoon.food.common.messaging.kafka.model;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantApprovalResponseModel {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("sagaId")
    private String sagaId;
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("restaurantId")
    private String restaurantId;
    
    @JsonProperty("createdAt")
    private Instant createdAt;
    
    @JsonProperty("orderApprovalStatus")
    private OrderApprovalStatus orderApprovalStatus;
    
    @JsonProperty("failureMessages")
    private List<String> failureMessages;
    
    public enum OrderApprovalStatus {
        APPROVED, REJECTED
    }
}