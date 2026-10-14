package com.ucb.market.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ProductDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @Positive(message = "O valor deve ser maior que zero")
    private Double value;

    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private int quantity;

    public ProductDTO() {
    }


}
