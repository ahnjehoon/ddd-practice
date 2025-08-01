package com.jehoon.food.restaurant.service.application.handler.command;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantCommand;
import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantResponse;
import com.jehoon.food.restaurant.service.application.mapper.RestaurantDataMapper;
import com.jehoon.food.restaurant.service.application.ports.output.repository.RestaurantRepository;
import com.jehoon.food.restaurant.service.domain.Restaurant;
import com.jehoon.food.restaurant.service.domain.RestaurantDomainService;
import com.jehoon.food.restaurant.service.domain.event.RestaurantCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CreateRestaurantCommandHandler {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDataMapper restaurantDataMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CreateRestaurantCommandHandler(RestaurantDomainService restaurantDomainService,
                                        RestaurantRepository restaurantRepository,
                                        RestaurantDataMapper restaurantDataMapper,
                                        ApplicationEventPublisher applicationEventPublisher) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantRepository = restaurantRepository;
        this.restaurantDataMapper = restaurantDataMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public CreateRestaurantResponse createRestaurant(CreateRestaurantCommand createRestaurantCommand) {
        log.info("Creating restaurant with id: {}", createRestaurantCommand.getRestaurantId());
        
        // Command를 도메인 객체로 변환
        Restaurant restaurant = restaurantDataMapper.createRestaurantCommandToRestaurant(createRestaurantCommand);
        
        // 도메인 서비스를 통한 비즈니스 규칙 검증 및 이벤트 생성
        RestaurantCreatedEvent restaurantCreatedEvent = restaurantDomainService.validateAndCreateRestaurant(restaurant);
        
        // Repository를 통해 저장
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        
        // 도메인 이벤트 발행
        applicationEventPublisher.publishEvent(restaurantCreatedEvent);
        
        log.info("Restaurant created with id: {}", savedRestaurant.getId().getValue());
        
        return restaurantDataMapper.restaurantToCreateRestaurantResponse(
                savedRestaurant, 
                "Restaurant created successfully!");
    }
}