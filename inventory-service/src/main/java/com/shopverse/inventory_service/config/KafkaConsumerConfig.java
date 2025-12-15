package com.shopverse.inventory_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopverse.inventory_service.dto.OrderEventDto;
import com.shopverse.inventory_service.model.InventoryEntity;
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
    //---------------------------- PRODUCT EVENTS (String) -----------------------------//
    @Bean
    public ConsumerFactory<String, String> productConsumerFactory(){
        Map<String,Object> config= new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"inventory-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }


    @Bean(name = "productKafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String,String> productKafkaListenerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,String> factory=
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(productConsumerFactory());
        return factory;
    }

    //---------------------------- ORDER EVENTS (JSON) -----------------------------//

    @Bean
    public ConsumerFactory<String, OrderEventDto> orderConsumerFactory(){
        Map<String,Object> config=new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"inventory-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Ignore the type info in headers
        JsonDeserializer<OrderEventDto> deserializer = new JsonDeserializer<>(OrderEventDto.class);
        deserializer.addTrustedPackages("*");
        deserializer.ignoreTypeHeaders();

        return new DefaultKafkaConsumerFactory<>(config,new StringDeserializer(),deserializer);

    }

        @Bean(name="orderListenerFactory")
        public ConcurrentKafkaListenerContainerFactory<String,OrderEventDto> orderKafkaListenerContainerFactory(){
            ConcurrentKafkaListenerContainerFactory<String,OrderEventDto> factory= new ConcurrentKafkaListenerContainerFactory<>();

            factory.setConsumerFactory(orderConsumerFactory());

            return factory;
        }

}
