package com.viniville.rinhabackend.api.transacao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record TransacaoInsertRequest (
        @JsonProperty(access = JsonProperty.Access.READ_ONLY) Long idCliente,
        @NotNull @Positive Long valor,
        @NotNull @Pattern(regexp = "^[c|d]$") String tipo,
        @NotNull @Size(min = 1, max = 10) String descricao
)
{
    public TransacaoInsertRequest withIdCliente(Long idCliente) {
        return new TransacaoInsertRequest(idCliente, valor, tipo, descricao);
    }
}