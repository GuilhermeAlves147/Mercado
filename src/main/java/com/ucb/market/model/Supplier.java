package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_supplier")
@Getter
@Setter
@NoArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_supplier")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "CNPJ", nullable = false, length = 14)
    private String cnpj;

    @Column(length = 20)
    private String phone;
}
