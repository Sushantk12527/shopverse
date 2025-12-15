package com.shopverse.order_service.service;

import com.shopverse.order_service.dto.*;
import com.shopverse.order_service.entity.OrderEntity;
import com.shopverse.order_service.entity.OrderItemEntity;
import com.shopverse.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ProductClient productClient;

    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public OrderEntity createOrder(OrderRequestDto orderRequest) {
        // Create order entity
        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        List<OrderItemEntity> items=new ArrayList<>();
        double totalAmount = 0.0;

        for(OrderItemRequestDto itemReq: orderRequest.getItems()){
            ProductResponseDto product=productClient.getProductById(itemReq.getProductId());
            if(product==null){
                throw new RuntimeException("Product not found: "+itemReq.getProductId());
            }

            OrderItemEntity item = new OrderItemEntity();
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice());
            item.setOrders(order);

            totalAmount += product.getPrice() * itemReq.getQuantity();
            items.add(item);
        }


        order.setTotalAmount(totalAmount);

        order.setItems(items);

        // Save order along with items (cascade = ALL ensures items are saved)
        orderRepository.save(order);
        OrderEventDto orderEventDto =OrderEventDto.builder()
                .orderId(order.getId())
                .userId(101L)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems().stream()
                        .map(i-> OrderItemEventDto.builder()
                                .productId(i.getProductId())
                                .quantity(i.getQuantity())
                                .price(i.getPrice())
                                .build())
                                .collect(Collectors.toList()))
                .build();

        kafkaTemplate.send("orderEvent", orderEventDto);
        System.out.println("Order Event sent: "+ orderEventDto);

        return order;
    }

    public OrderEntity getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}
