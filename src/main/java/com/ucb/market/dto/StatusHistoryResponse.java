package com.ucb.market.dto;

import com.ucb.market.model.OrderStatusType;

import java.time.LocalDateTime;

public record StatusHistoryResponse(LocalDateTime date, OrderStatusType status) { }
