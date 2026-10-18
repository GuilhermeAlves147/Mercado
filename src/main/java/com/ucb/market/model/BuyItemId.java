package com.ucb.market.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BuyItemId implements Serializable {

    @Column(name = "id_product")
    private Integer productId;

    @Column(name = "id_cart")
    private Integer cartId;
}
