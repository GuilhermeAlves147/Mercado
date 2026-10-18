package com.ucb.market.repository;

import com.ucb.market.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    List<PurchaseOrder> findByClientIdOrderByDateOrderDesc(Integer clientId);
}
