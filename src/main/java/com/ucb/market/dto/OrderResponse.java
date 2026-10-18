package com.ucb.market.dto;

import com.ucb.market.model.OrderStatusType;
import com.ucb.market.model.PurchaseOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Integer id, LocalDateTime dateOrder, BigDecimal totalValue,
                            OrderStatusType currentStatus, List<OrderItemResponse> items,
                            List<StatusHistoryResponse> history) {
    public static OrderResponse from(PurchaseOrder o) {
        return new OrderResponse(o.getId(), o.getDateOrder(), o.getTotalValue(), o.currentStatus(),
                o.getItems().stream().map(OrderItemResponse::from).toList(),
                o.getStatuses().stream()
                        .map(s -> new StatusHistoryResponse(s.getId().getDateStatus(), s.getStatus()))
                        .toList());
    }
}