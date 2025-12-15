package com.shopverse.inventory_service.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopverse.inventory_service.dto.InventoryStatusEventDto;
import com.shopverse.inventory_service.dto.OrderEventDto;
import com.shopverse.inventory_service.dto.OrderItemEventDto;
import com.shopverse.inventory_service.model.InventoryEntity;
import com.shopverse.inventory_service.repository.InventoryRepository;
import com.shopverse.order_service.dto.InventoryStatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventConsumer {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;
    private final InventoryEventProducer inventoryEventProducer;


    public InventoryEventConsumer(InventoryRepository inventoryRepository, ObjectMapper objectMapper, InventoryEventProducer inventoryEventProducer) {
        this.inventoryRepository = inventoryRepository;
        this.objectMapper = objectMapper;
        this.inventoryEventProducer = inventoryEventProducer;
    }

    @KafkaListener(topics="productEvents",groupId = "inventory-group")
    public void consumeProductEvent(String message){
        System.out.println("Consumed product event: "+message);

        try{
            //Convert Json String to Java Object
            JsonNode event=objectMapper.readTree(message);

            // CASE - DELETE PRODUCT

            if(event.has("eventType") && event.get("eventType").asText().equals("DELETE")){
                Long productId=event.get("productId").asLong();

                InventoryEntity existing=inventoryRepository.findByProductId(productId);

                if(existing!=null){
                    inventoryRepository.delete(existing);
                    System.out.println("Inventory removed for Product ID: "+ productId);
                }

                return;
            }

            // CASE - ADD/UPDATE PRODUCT EVENT
            Long productId=event.get("id").asLong();
            String productName=event.get("name").asText();
            Integer quantity=event.get("stock").asInt();

            InventoryEntity existing=inventoryRepository.findByProductId(productId);

            if(existing !=null){
                // Update inventory if product already exists
                existing.setProductName(productName);
                existing.setQuantity(quantity);

                inventoryRepository.save(existing);
                System.out.println("Inventory updated for: " + productName);
            }else{
                InventoryEntity inventoryEntity=new InventoryEntity();
                inventoryEntity.setProductId(productId);
                inventoryEntity.setProductName(productName);
                inventoryEntity.setQuantity(quantity);

                inventoryRepository.save(inventoryEntity);
                System.out.println("Saved to Inventory DB: "+productName );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "orderEvent",groupId = "inventory-group",containerFactory = "orderListenerFactory")
    public void consumeOrderEvent(OrderEventDto order) {

           System.out.println("🛒 Order Event Received: " + order);

           boolean allAvailable=true;

           for(OrderItemEventDto item: order.getItems()){
               InventoryEntity entity=inventoryRepository.findByProductId(item.getProductId());

               if (entity.getQuantity() < item.getQuantity()) {
                   allAvailable = false;
                   break;
               }
           }

           if(allAvailable){
               InventoryStatusEventDto response= InventoryStatusEventDto.builder()
                       .orderId(order.getOrderId())
                       .status("ORDER CONFIRMED")
                       .build();
               inventoryEventProducer.sendStatus(response);
               System.out.println("Inventory status response sent");


              /* //Deduct Stock
               order.getItems().forEach(item->{
                   InventoryEntity inv = inventoryRepository.findByProductId(item.getProductId());
                   inv.setQuantity(inv.getQuantity() - item.getQuantity());
                   inventoryRepository.save(inv);
               });
               System.out.println("📦 Inventory Updated Successfully.");
*/
           }else{
               InventoryStatusEventDto response= InventoryStatusEventDto.builder()
                       .orderId(order.getOrderId())
                       .status("ORDER REJECTED")
                       .build();
               inventoryEventProducer.sendStatus(response);
               System.out.println("Inventory status response sent");

           }
    }
}
