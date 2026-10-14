package com.ucb.market.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "Usuário é obrigatório") @Size(max = 50) String username,
        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,
        @NotBlank @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres") String password) { }