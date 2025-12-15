package com.shopverse.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

    private Long userId; // we'll remove it when implementing authorization
    private List<OrderItemRequestDto> items;



    public List<OrderItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDto> items) {
        this.items = items;
    }
}

