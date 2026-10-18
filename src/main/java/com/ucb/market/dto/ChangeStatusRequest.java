package com.ucb.market.dto;

import com.ucb.market.model.OrderStatusType;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(@NotNull OrderStatusType status) { }
