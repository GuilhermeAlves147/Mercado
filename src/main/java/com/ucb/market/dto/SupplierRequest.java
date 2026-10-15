package com.ucb.market.dto;

import jakarta.validation.constraints.*;

public record SupplierRequest(@NotBlank @Size(max = 100) String name,
                              @NotBlank @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter 14 dígitos") String cnpj,
                              @Size(max = 20) String phone) { }

