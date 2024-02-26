package com.viniville.rinhabackend.api.transacao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record ExtratoResponseTransacao(
        Long valor,
        String tipo,
        String descricao,
        @JsonProperty("realizada_em") OffsetDateTime realizadaEm
) {}
