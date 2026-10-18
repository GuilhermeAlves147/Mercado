package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_order_status")
@Getter
@Setter
@NoArgsConstructor
public class OrderStatus {

    @EmbeddedId
    private OrderStatusId id = new OrderStatusId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "id_order")
    private PurchaseOrder order;

    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatusType status;

    public OrderStatus(PurchaseOrder order, LocalDateTime dateStatus, OrderStatusType status) {
        this.order = order;
        this.id.setDateStatus(dateStatus);
        this.status = status;
    }
}
