package com.ucb.market.dto;

import jakarta.validation.constraints.Size;

public record ClientRequest(@Size(max = 20) String phone) { }
