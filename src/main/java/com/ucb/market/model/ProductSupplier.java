package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_product_supplier")
@Getter
@Setter
@NoArgsConstructor
public class ProductSupplier {

    @EmbeddedId
    private ProductSupplierId id = new ProductSupplierId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("supplierId")
    @JoinColumn(name = "id_supplier")
    private Supplier supplier;

    @Column(name = "delivery_time", nullable = false)
    private Integer deliveryTime;
}