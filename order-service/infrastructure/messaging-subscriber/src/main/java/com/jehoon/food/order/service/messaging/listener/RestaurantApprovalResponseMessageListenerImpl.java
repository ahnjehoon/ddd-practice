package com.jehoon.food.order.service.messaging.listener;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.jehoon.food.order.application.dto.message.RestaurantApprovalResponse;
import com.jehoon.food.order.application.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
