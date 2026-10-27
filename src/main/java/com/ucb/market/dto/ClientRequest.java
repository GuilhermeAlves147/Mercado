package com.ucb.market.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** O perfil de cliente utiliza telefone; CNPJ pertence ao vendedor.
 *  O campo legado cnpj permanece no request para compatibilidade com clientes antigos,
 *  mas não é persistido na tabela tb_client. */
public record ClientRequest(
        @Size(max = 20) String phone,
        @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 números") String cnpj
) {}
