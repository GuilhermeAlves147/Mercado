package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_buyItem")
@Getter
@Setter
@NoArgsConstructor
public class BuyItem {

    @EmbeddedId
    private BuyItemId id = new BuyItemId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "id_cart")
    private Cart cart;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitValue;

    public BuyItem(Cart cart, Product product, int quantity, BigDecimal unitValue) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.unitValue = unitValue;
    }
}