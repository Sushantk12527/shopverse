package com.shopverse.inventory_service.event;

import com.shopverse.inventory_service.dto.InventoryStatusEventDto;
import com.shopverse.order_service.dto.InventoryStatusEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, InventoryStatusEventDto> kafkaTemplate;

    public void sendStatus(InventoryStatusEventDto event){
        kafkaTemplate.send("inventoryStatusEvent",event);
        System.out.println("📦 Inventory Response Sent → " + event);
    }
}
