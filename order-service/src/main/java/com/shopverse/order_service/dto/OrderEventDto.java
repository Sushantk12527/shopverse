package com.shopverse.order_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderEventDto {
    private Long orderId;
    private Long userId;
    private List<OrderItemEventDto> items;
    private double totalAmount;
    private String status;

    public OrderEventDto() {
    }

    public OrderEventDto(Long orderId, Long userId, List<OrderItemEventDto> items, double totalAmount, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemEventDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEventDto> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderEventDto{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", items=" + items +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
