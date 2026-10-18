package com.ucb.market.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderStatusId implements Serializable {

    @Column(name = "id_order")
    private Integer orderId;

    @Column(name = "date_status")
    private LocalDateTime dateStatus;
}
