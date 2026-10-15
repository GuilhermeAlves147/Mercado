package com.ucb.market.dto;

import com.ucb.market.model.Supplier;

public record SupplierResponse(Integer id, String name, String cnpj, String phone) {
    public static SupplierResponse from(Supplier s) {
        return new SupplierResponse(s.getId(), s.getName(), s.getCnpj(), s.getPhone());
    }
}
