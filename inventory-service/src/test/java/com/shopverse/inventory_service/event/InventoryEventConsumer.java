package com.shopverse.inventory_service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopverse.inventory_service.model.InventoryEntity;
import com.shopverse.inventory_service.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventConsumer {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;


    public InventoryEventConsumer(InventoryRepository inventoryRepository, ObjectMapper objectMapper) {
        this.inventoryRepository = inventoryRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics="productEvents",groupId = "inventory-group")
    public void consumeProductEvent(String message){
        System.out.println("Consumed product event: "+message);

        try{
            //Convert Json String to Java Object
            JsonNode event=objectMapper.readTree(message);
            System.out.println("JSON KEYS RECEIVED:");
            event.fieldNames().forEachRemaining(System.out::println);

            Long productId=event.get("id").asLong();
            String productName=event.get("name").asText();
            Integer stock = event.get("stock").asInt();

            InventoryEntity inventoryEntity=new InventoryEntity();
            inventoryEntity.setProductId(productId);
            inventoryEntity.setProductName(productName);
            inventoryEntity.setQuantity(stock);

            inventoryRepository.save(inventoryEntity);
            System.out.println("Saved to Inventory DB: "+productName );

        } catch (Exception e) {
            System.out.println("❌ ERROR PARSING MESSAGE:");
            System.out.println("RAW MESSAGE: " + message);
            e.printStackTrace();
        }
    }
}
