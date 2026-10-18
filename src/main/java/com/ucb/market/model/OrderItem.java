package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_orderItem")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @EmbeddedId
    private OrderItemId id = new OrderItemId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "id_order")
    private PurchaseOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "id_product")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitValue;

    public OrderItem(PurchaseOrder order, Product product, int quantity, BigDecimal unitValue) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitValue = unitValue;
    }
}
