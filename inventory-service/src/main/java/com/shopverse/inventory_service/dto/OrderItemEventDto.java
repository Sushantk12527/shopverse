package com.shopverse.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEventDto {
    private Long productId;
    private Integer quantity;

    @Override
    public String toString() {
        return "OrderItemEventDto{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
