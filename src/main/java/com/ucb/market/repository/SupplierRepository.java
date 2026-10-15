package com.ucb.market.repository;

import com.ucb.market.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    boolean existsByCnpj(String cnpj);
}