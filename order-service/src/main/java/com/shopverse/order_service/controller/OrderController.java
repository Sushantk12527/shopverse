package com.shopverse.order_service.controller;

import com.shopverse.order_service.dto.OrderRequestDto;
import com.shopverse.order_service.entity.OrderEntity;
import com.shopverse.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<OrderEntity> placeOrder(@RequestBody OrderRequestDto orderRequest) {
        OrderEntity savedOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(savedOrder);
    }

    //Get single order
    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        List<OrderEntity> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
