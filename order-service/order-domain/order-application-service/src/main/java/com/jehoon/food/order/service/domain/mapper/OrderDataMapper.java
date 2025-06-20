package com.jehoon.food.order.service.domain.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jehoon.food.domain.valueobject.CustomerId;
import com.jehoon.food.domain.valueobject.Money;
import com.jehoon.food.domain.valueobject.ProductId;
import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.order.service.domain.dto.create.CreateOrderCommand;
import com.jehoon.food.order.service.domain.dto.create.CreateOrderResponse;
import com.jehoon.food.order.service.domain.dto.create.OrderAddressDto;
import com.jehoon.food.order.service.domain.dto.create.OrderItemDto;
import com.jehoon.food.order.service.domain.dto.track.TrackOrderResponse;
import com.jehoon.food.order.service.domain.entity.Order;
import com.jehoon.food.order.service.domain.entity.OrderItem;
import com.jehoon.food.order.service.domain.entity.Product;
import com.jehoon.food.order.service.domain.entity.Restaurant;
import com.jehoon.food.order.service.domain.valueobject.StreetAddress;

@Component
public class OrderDataMapper {

        public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
                return Restaurant.builder()
                                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                                .products(createOrderCommand.getItems().stream()
                                                .map(orderItem -> new Product(new ProductId(orderItem.getProductId())))
                                                .collect(Collectors.toList()))
                                .build();
        }

        public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
                return Order.builder()
                                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                                .streetAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                                .price(new Money(createOrderCommand.getPrice()))
                                .orderItems(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                                .build();
        }

        public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
                return CreateOrderResponse.builder()
                                .orderTrackingId(order.getTrackingId().getValue())
                                .orderStatus(order.getOrderStatus())
                                .message(message)
                                .build();
        }

        public TrackOrderResponse orderToTrackOrderResponse(Order order) {
                return TrackOrderResponse.builder()
                                .orderTrackingId(order.getTrackingId().getValue())
                                .orderStatus(order.getOrderStatus())
                                .failureMessages(order.getFailureMessages())
                                .build();
        }

        private List<OrderItem> orderItemsToOrderItemEntities(List<OrderItemDto> orderItems) {
                return orderItems.stream()
                                .map(orderItem -> OrderItem.builder()
                                                .product(new Product(new ProductId(orderItem.getProductId())))
                                                .price(new Money(orderItem.getPrice()))
                                                .quantity(orderItem.getQuantity())
                                                .subTotal(new Money(orderItem.getSubTotal()))
                                                .build())
                                .collect(Collectors.toList());
        }

        private StreetAddress orderAddressToStreetAddress(OrderAddressDto orderAddress) {
                return new StreetAddress(
                                UUID.randomUUID(),
                                orderAddress.getStreet(),
                                orderAddress.getPostalCode(),
                                orderAddress.getCity());
        }
}
