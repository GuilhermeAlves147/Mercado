package com.ucb.market.repository;

import com.ucb.market.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    boolean existsByCnpj(String cnpj);
}
