package com.ucb.market.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusType {
    ABERTO("aberto"), PAGO("pago"), ENVIADO("enviado"),
    ENTREGUE("entregue"), CANCELADO("cancelado");

    private final String dbValue;

    public static OrderStatusType fromDb(String value) {
        for (OrderStatusType s : values()) {
            if (s.dbValue.equals(value)) return s;
        }
        throw new IllegalArgumentException("Status desconhecido: " + value);
    }
}
