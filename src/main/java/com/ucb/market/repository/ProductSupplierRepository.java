package com.ucb.market.repository;

import com.ucb.market.model.ProductSupplier;
import com.ucb.market.model.ProductSupplierId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSupplierRepository
        extends JpaRepository<ProductSupplier, ProductSupplierId> {
    List<ProductSupplier> findByIdProductId(Integer productId);
}
