package com.shopverse.order_service.client;

import com.shopverse.order_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="product-service",url = "http://localhost:8080")
public interface ProductServiceClient {

    @GetMapping("/products/{id}")
    ProductResponseDto getProductBYId(@PathVariable("id") Long productId);
}
