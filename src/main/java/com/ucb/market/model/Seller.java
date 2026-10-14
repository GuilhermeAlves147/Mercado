package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_seller")
@Getter
@Setter
@NoArgsConstructor
public class Seller {

    @Id
    @Column(name = "id_seller")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_seller")
    private User user;

    @Column(name = "CNPJ", nullable = false, length = 14)
    private String cnpj;
}