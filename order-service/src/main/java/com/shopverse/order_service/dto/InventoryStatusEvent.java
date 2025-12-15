package com.shopverse.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStatusEvent {
    private Long orderId;
    private String status; // "SUCCESS" or "FAILED"
}
