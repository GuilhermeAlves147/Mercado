package com.ucb.market.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LinkSupplierRequest(@NotNull Integer supplierId,
                                  @NotNull @Positive Integer deliveryTime) { }