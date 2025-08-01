package com.jehoon.food.restaurant.service.application.handler.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.jehoon.food.restaurant.service.domain.event.OrderApprovedEvent;
import com.jehoon.food.restaurant.service.domain.event.OrderRejectedEvent;
import com.jehoon.food.restaurant.service.domain.event.RestaurantCreatedEvent;
import com.jehoon.food.restaurant.service.domain.event.RestaurantUpdatedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestaurantEventHandler {

    @EventListener
    public void handleRestaurantCreatedEvent(RestaurantCreatedEvent event) {
        log.info("Restaurant created event received for restaurant id: {}", 
                event.getRestaurant().getId().getValue());
        
        // TODO: Read Model 업데이트, 외부 시스템 통지 등
        // 예: Kafka 메시지 발행, 검색 인덱스 업데이트, 캐시 업데이트
        updateRestaurantReadModel(event);
        notifyExternalSystems(event);
    }

    @EventListener
    public void handleRestaurantUpdatedEvent(RestaurantUpdatedEvent event) {
        log.info("Restaurant updated event received for restaurant id: {}", 
                event.getRestaurant().getId().getValue());
        
        // TODO: Read Model 업데이트
        updateRestaurantReadModel(event);
    }

    @EventListener
    public void handleOrderApprovedEvent(OrderApprovedEvent event) {
        log.info("Order approved event received for order id: {} from restaurant id: {}", 
                event.getOrderId().getValue(), 
                event.getRestaurant().getId().getValue());
        
        // TODO: 주문 시스템에 승인 결과 전송
        sendOrderApprovalToOrderService(event);
    }

    @EventListener
    public void handleOrderRejectedEvent(OrderRejectedEvent event) {
        log.info("Order rejected event received for order id: {} from restaurant id: {}, reason: {}", 
                event.getOrderId().getValue(), 
                event.getRestaurant().getId().getValue(),
                event.getRejectionReason());
        
        // TODO: 주문 시스템에 거부 결과 전송
        sendOrderRejectionToOrderService(event);
    }

    private void updateRestaurantReadModel(RestaurantCreatedEvent event) {
        // TODO: Read Model 업데이트 로직
        log.debug("Updating read model for restaurant: {}", event.getRestaurant().getId().getValue());
    }

    private void updateRestaurantReadModel(RestaurantUpdatedEvent event) {
        // TODO: Read Model 업데이트 로직
        log.debug("Updating read model for restaurant: {}", event.getRestaurant().getId().getValue());
    }

    private void notifyExternalSystems(RestaurantCreatedEvent event) {
        // TODO: 외부 시스템 통지 로직
        log.debug("Notifying external systems for restaurant: {}", event.getRestaurant().getId().getValue());
    }

    private void sendOrderApprovalToOrderService(OrderApprovedEvent event) {
        // TODO: Kafka를 통해 Order Service에 승인 결과 전송
        log.debug("Sending order approval to order service: {}", event.getOrderId().getValue());
    }

    private void sendOrderRejectionToOrderService(OrderRejectedEvent event) {
        // TODO: Kafka를 통해 Order Service에 거부 결과 전송
        log.debug("Sending order rejection to order service: {}", event.getOrderId().getValue());
    }
}