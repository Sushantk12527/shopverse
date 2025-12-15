package com.shopverse.product_service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopverse.product_service.model.Product;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductEventProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private static final String TOPIC = "productEvents";
    private final ObjectMapper objectMapper=new ObjectMapper();



    public ProductEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendProductCreatedEvent(Product product){
        try {
            String productJson=objectMapper.writeValueAsString(product);
            kafkaTemplate.send(TOPIC,productJson);
            System.out.println("Sent message to Kafka: "+ productJson);
        } catch (Exception e) {
            throw new RuntimeException("Error sending event",e);
        }
    }

    public void sendProductUpdatedEvent(Product product){
        try{
            String productJson=objectMapper.writeValueAsString(product);
            kafkaTemplate.send(TOPIC,productJson);
            System.out.println("Sent Product Update Event: "+productJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendProductDeleteEvent(Long id) {

        try{
            Map<String,Object> event= new HashMap<>();
            event.put("eventType","DELETE");
            event.put("productId",id);

            String productJson= objectMapper.writeValueAsString(event);

            kafkaTemplate.send(TOPIC,productJson);
            System.out.println("Sent Delete Evnet: "+ productJson);

        } catch (Exception e) {
            throw new RuntimeException("Error sending delete event",e);
        }
    }
}
