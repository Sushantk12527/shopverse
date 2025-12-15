package com.shopverse.product_service.controller;

import com.shopverse.product_service.event.ProductEventProducer;
import com.shopverse.product_service.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductEventController {

    private final ProductEventProducer producer;

    public ProductEventController( ProductEventProducer producer) {
        this.producer = producer;
    }

    // Endpoint to send message: http://localhost:8080/send?message=Hello
    @GetMapping("/send")
    public String sendProduct(@RequestParam Product product){
        producer.sendProductCreatedEvent(product);
        return "Sent Product: " +product.getName();

    }
}
