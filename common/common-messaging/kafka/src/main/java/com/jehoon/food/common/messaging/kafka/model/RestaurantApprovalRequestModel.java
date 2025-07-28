package com.jehoon.food.common.messaging.kafka.model;

import java.math.BigDecimal;
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
public class RestaurantApprovalRequestModel {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("sagaId")
    private String sagaId;
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("restaurantId")
    private String restaurantId;
    
    @JsonProperty("restaurantOrderStatus")
    private RestaurantOrderStatus restaurantOrderStatus;
    
    @JsonProperty("products")
    private List<Product> products;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("createdAt")
    private Instant createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("quantity")
        private Integer quantity;
    }
    
    public enum RestaurantOrderStatus {
        PAID
    }
}