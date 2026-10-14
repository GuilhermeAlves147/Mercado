package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_client")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @Column(name = "id_client")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_client")
    private User user;

    @Column(length = 20)
    private String phone;
}