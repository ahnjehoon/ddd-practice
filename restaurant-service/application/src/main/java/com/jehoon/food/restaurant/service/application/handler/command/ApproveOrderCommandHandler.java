package com.jehoon.food.restaurant.service.application.handler.command;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderCommand;
import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderResponse;
import com.jehoon.food.restaurant.service.application.mapper.RestaurantDataMapper;
import com.jehoon.food.restaurant.service.application.ports.output.repository.RestaurantRepository;
import com.jehoon.food.restaurant.service.domain.Product;
import com.jehoon.food.restaurant.service.domain.Restaurant;
import com.jehoon.food.restaurant.service.domain.RestaurantDomainException;
import com.jehoon.food.restaurant.service.domain.RestaurantDomainService;
import com.jehoon.food.restaurant.service.domain.event.OrderApprovedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApproveOrderCommandHandler {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDataMapper restaurantDataMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ApproveOrderCommandHandler(RestaurantDomainService restaurantDomainService,
                                    RestaurantRepository restaurantRepository,
                                    RestaurantDataMapper restaurantDataMapper,
                                    ApplicationEventPublisher applicationEventPublisher) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantRepository = restaurantRepository;
        this.restaurantDataMapper = restaurantDataMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public ApproveOrderResponse approveOrder(ApproveOrderCommand approveOrderCommand) {
        log.info("Approving order with id: {} for restaurant: {}", 
                approveOrderCommand.getOrderId(), approveOrderCommand.getRestaurantId());
        
        RestaurantId restaurantId = new RestaurantId(approveOrderCommand.getRestaurantId());
        OrderId orderId = new OrderId(approveOrderCommand.getOrderId());
        
        // Restaurant 조회
        Restaurant restaurant = restaurantRepository.findByIdWithProducts(restaurantId)
                .orElseThrow(() -> new RestaurantDomainException(
                        "Restaurant with id " + restaurantId.getValue() + " not found"));
        
        // Command에서 상품 정보 추출
        List<Product> orderProducts = restaurantDataMapper.approveOrderCommandToProducts(approveOrderCommand);
        
        // 도메인 서비스를 통한 주문 승인 처리
        OrderApprovedEvent orderApprovedEvent = restaurantDomainService.approveOrder(restaurant, orderId, orderProducts);
        
        // 도메인 이벤트 발행
        applicationEventPublisher.publishEvent(orderApprovedEvent);
        
        log.info("Order approved with id: {} for restaurant: {}", orderId.getValue(), restaurantId.getValue());
        
        return restaurantDataMapper.orderIdToApproveOrderResponse(orderId, "Order approved successfully!");
    }
}