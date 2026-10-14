package com.ucb.market.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(@NotBlank @Size(max = 100) String name,
                              @Size(max = 255) String description,
                              Integer parentId) { }
