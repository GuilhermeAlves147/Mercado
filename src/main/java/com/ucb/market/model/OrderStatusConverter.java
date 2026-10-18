package com.ucb.market.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatusType, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatusType status) {
        return status == null ? null : status.getDbValue();
    }

    @Override
    public OrderStatusType convertToEntityAttribute(String db) {
        return db == null ? null : OrderStatusType.fromDb(db);
    }
}
