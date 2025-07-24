package com.jehoon.food.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {
        "com.jehoon.food.order.service.dataaccess",
        "com.jehoon.food.dataaccess" })
@EntityScan(basePackages = {
        "com.jehoon.food.order.service.dataaccess",
        "com.jehoon.food.dataaccess" })
@SpringBootApplication(scanBasePackages = "com.jehoon.food")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}