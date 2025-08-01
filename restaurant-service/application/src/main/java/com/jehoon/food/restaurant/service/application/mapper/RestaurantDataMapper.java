package com.jehoon.food.restaurant.service.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jehoon.food.domain.valueobject.Money;
import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.domain.valueobject.ProductId;
import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderCommand;
import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderResponse;
import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantCommand;
import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantResponse;
import com.jehoon.food.restaurant.service.application.dto.command.UpdateProductCommand;
import com.jehoon.food.restaurant.service.application.dto.command.UpdateProductResponse;
import com.jehoon.food.restaurant.service.application.dto.query.FindRestaurantResponse;
import com.jehoon.food.restaurant.service.domain.Product;
import com.jehoon.food.restaurant.service.domain.Restaurant;

@Component
public class RestaurantDataMapper {

    public Restaurant createRestaurantCommandToRestaurant(CreateRestaurantCommand createRestaurantCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createRestaurantCommand.getRestaurantId()))
                .name(createRestaurantCommand.getName())
                .active(createRestaurantCommand.getActive())
                .products(createRestaurantCommand.getProducts().stream()
                        .map(this::productDtoToProduct)
                        .collect(Collectors.toList()))
                .build();
    }

    public CreateRestaurantResponse restaurantToCreateRestaurantResponse(Restaurant restaurant, String message) {
        return CreateRestaurantResponse.builder()
                .restaurantId(restaurant.getId().getValue())
                .message(message)
                .build();
    }

    public Product updateProductCommandToProduct(UpdateProductCommand updateProductCommand) {
        return Product.builder()
                .productId(new ProductId(updateProductCommand.getProductId()))
                .name(updateProductCommand.getName())
                .price(new Money(updateProductCommand.getPrice()))
                .quantity(updateProductCommand.getQuantity())
                .available(updateProductCommand.getAvailable())
                .build();
    }

    public UpdateProductResponse productToUpdateProductResponse(Product product, String message) {
        return UpdateProductResponse.builder()
                .productId(product.getId().getValue())
                .message(message)
                .build();
    }

    public List<Product> approveOrderCommandToProducts(ApproveOrderCommand approveOrderCommand) {
        return approveOrderCommand.getProducts().stream()
                .map(orderProduct -> Product.builder()
                        .productId(new ProductId(orderProduct.getProductId()))
                        .name(orderProduct.getName())
                        .quantity(orderProduct.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    public ApproveOrderResponse orderIdToApproveOrderResponse(OrderId orderId, String message) {
        return ApproveOrderResponse.builder()
                .orderId(orderId.getValue())
                .message(message)
                .build();
    }

    public FindRestaurantResponse restaurantToFindRestaurantResponse(Restaurant restaurant) {
        return FindRestaurantResponse.builder()
                .restaurantId(restaurant.getId().getValue())
                .name(restaurant.getName())
                .active(restaurant.isActive())
                .products(restaurant.getProducts().stream()
                        .map(this::productToProductDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private Product productDtoToProduct(CreateRestaurantCommand.ProductDto productDto) {
        return Product.builder()
                .productId(new ProductId(productDto.getProductId()))
                .name(productDto.getName())
                .price(new Money(productDto.getPrice()))
                .quantity(productDto.getQuantity())
                .available(productDto.getAvailable())
                .build();
    }

    private FindRestaurantResponse.ProductDto productToProductDto(Product product) {
        return FindRestaurantResponse.ProductDto.builder()
                .productId(product.getId().getValue())
                .name(product.getName())
                .price(product.getPrice().getAmount())
                .quantity(product.getQuantity())
                .available(product.isAvailable())
                .build();
    }
}