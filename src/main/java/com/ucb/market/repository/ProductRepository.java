package com.ucb.market.repository;

import com.ucb.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findBySellerId(Integer sellerId);
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :qty " +
            "where p.id = :id and p.quantity >= :qty")
    int decreaseStock(@Param("id") Integer id, @Param("qty") int qty);

    @Modifying
    @Query("update Product p set p.quantity = p.quantity + :qty where p.id = :id")
    int increaseStock(@Param("id") Integer id, @Param("qty") int qty);
}
