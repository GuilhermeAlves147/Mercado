package com.ucb.market.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Double value;
    private int quantity;

    public Product(String name, double value, int quantity,int id) {
        this.name = name;
        this.value = value;
        this.quantity = quantity;
        this.id = id;
    }
    public Product(){}


}