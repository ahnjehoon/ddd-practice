package com.jehoon.food.common.messaging.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {
    private String bootstrapServers;
    private Integer numOfPartitions;
    private Short replicationFactor;
}
