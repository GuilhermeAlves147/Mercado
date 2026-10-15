package com.ucb.market.dto;

import com.ucb.market.model.ProductSupplier;

public record ProductSupplierResponse(Integer supplierId, String supplierName, Integer deliveryTime) {
    public static ProductSupplierResponse from(ProductSupplier ps) {
        return new ProductSupplierResponse(ps.getSupplier().getId(),
                ps.getSupplier().getName(), ps.getDeliveryTime());
    }
}
