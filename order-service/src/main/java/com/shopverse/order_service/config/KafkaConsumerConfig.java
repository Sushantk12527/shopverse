package com.shopverse.order_service.config;

import com.shopverse.order_service.dto.InventoryStatusEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, InventoryStatusEvent> inventoryStatusEventConsumerFactory(){
        Map<String,Object> config= new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"order-inventory-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<InventoryStatusEvent> deserializer=new JsonDeserializer<>(InventoryStatusEvent.class);
        deserializer.addTrustedPackages("*"); // allows DTO mapping; tighten later if desired.
        deserializer.ignoreTypeHeaders(); //prevents class-name mismatch problems.

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean(name="inventoryStatusListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String,InventoryStatusEvent> inventoryStatusListenerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,InventoryStatusEvent> factory=new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(inventoryStatusEventConsumerFactory());

        return factory;
    }
}
