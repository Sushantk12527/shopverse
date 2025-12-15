package com.shopverse.order_service.event;

import com.shopverse.order_service.dto.InventoryStatusEvent;
import com.shopverse.order_service.entity.OrderEntity;
import com.shopverse.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryStatusListener {

    private final OrderRepository orderRepository;

    public InventoryStatusListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "inventoryStatusEvent"
            ,groupId = "order-group"
            ,containerFactory = "inventoryStatusListenerFactory")
    @Transactional
    public void handleInventoryStatus(InventoryStatusEvent event){
        System.out.println("📥 InventoryStatusEvent received: " + event);

        Long orderId= event.getOrderId();
        String status=event.getStatus();

        OrderEntity orderEntity= orderRepository.findById(orderId).orElse(null);

        if(orderEntity==null){
            System.out.println("⚠ Order not found: " + orderId);
            return;
        }

        if(status.equalsIgnoreCase("ORDER CONFIRMED")){

            orderEntity.setStatus(status);
            orderRepository.save(orderEntity);
            System.out.println("✅ Order " + orderId + " confirmed. Proceed to payment.");
            // TODO: publish payment request event / call Payment service
        }else{
            orderEntity.setStatus("ORDER REJECTED");
            orderRepository.save(orderEntity);
            System.out.println("❌ Order " + orderId + " failed due to insufficient inventory.");
            // TODO: publish order-cancel event / refund / notify user
        }
    }
}
