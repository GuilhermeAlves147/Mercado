package com.ucb.market.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private int id_product;
    private String name;
    private Double value;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    public Product(String name, double value, int quantity,int id_product) {
        this.name = name;
        this.value = value;
        this.quantity = quantity;
        this.id_product = id_product;
    }
    public Product(){}


}