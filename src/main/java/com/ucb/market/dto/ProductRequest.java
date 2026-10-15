package com.ucb.market.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 100) String name,
        @NotNull @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero") BigDecimal value,
        @NotNull @PositiveOrZero(message = "A quantidade não pode ser negativa") Integer quantity,
        @NotNull Integer sellerId,
        @NotNull Integer categoryId) { }
