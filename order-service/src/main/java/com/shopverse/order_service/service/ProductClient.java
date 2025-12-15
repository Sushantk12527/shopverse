package com.shopverse.order_service.service;

import com.shopverse.order_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service",url = "http://localhost:8080")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductResponseDto getProductById(@PathVariable Long id);
}
