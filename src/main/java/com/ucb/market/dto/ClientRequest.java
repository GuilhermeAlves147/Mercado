package com.ucb.market.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** CNPJ opcional: compras existentes criam o perfil de cliente automaticamente. */
public record ClientRequest(
        @Size(max = 20) String phone,
        @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 números") String cnpj
) {}
