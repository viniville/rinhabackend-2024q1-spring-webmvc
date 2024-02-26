package com.viniville.rinhabackend.repository;

import com.viniville.rinhabackend.api.transacao.*;
import com.viniville.rinhabackend.exception.ClienteNaoExisteException;
import com.viniville.rinhabackend.exception.SaldoInsuficienteException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TransacaoRepository {
    private static final String SQL_UPDATE_SALDO_CLIENTE = """
                WITH results AS (
                    UPDATE public.cliente
                    SET saldo = saldo + :valorTransacao
                    WHERE id = :idCliente
                    RETURNING limite, saldo
                )\s
                SELECT * FROM results
            """;

    private static final String SQL_INSERT_TRANSACAO = """
            INSERT INTO public.transacao (id_cliente, descricao, tipo, valor)
            VALUES(:idCliente, :descricao, :tipo, :valorTransacao);
            """;

    private static final String SQL_EXTRATO = """
                select
                	c.saldo,
                	c.limite,
                	t.valor,
                	t.tipo,
                	t.descricao,
                	t.realizada_em
                from
                	public.cliente c
                	left join public.transacao t on (t.id_cliente = c.id)
                where
                	c.id = :idCliente
                limit 10
            """;

    private final JdbcClient jdbcClient;

    public TransacaoRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    public TransacaoInsertResponse registrarTransacao(TransacaoInsertRequest transacaoInsertRequest) {
        final var result = jdbcClient
                .sql(SQL_UPDATE_SALDO_CLIENTE)
                .param("valorTransacao", valorTransacaoParaSomaSaldo(transacaoInsertRequest))
                .param("idCliente", transacaoInsertRequest.idCliente())
                .query(TransacaoInsertResponse.class)
                .optional()
                .orElseThrow(() -> new ClienteNaoExisteException("Cliente não encontrato"));
        if (result.saldo() < 0 && (-result.saldo()) > result.limite()) {
            throw new SaldoInsuficienteException("saldo insuficiente");
        }
        jdbcClient
                .sql(SQL_INSERT_TRANSACAO)
                .param("idCliente", transacaoInsertRequest.idCliente())
                .param("descricao", transacaoInsertRequest.descricao())
                .param("tipo", transacaoInsertRequest.tipo())
                .param("valorTransacao", transacaoInsertRequest.valor())
                .update();
        return result;
    }

    public ExtratoResponse extrato(Long idCliente) {
        final var result = jdbcClient
                .sql(SQL_EXTRATO)
                .param("idCliente", idCliente)
                .query()
                .listOfRows();
        if (result.isEmpty()) {
            throw new ClienteNaoExisteException("Cliente não encontrato");
        }
        ExtratoResponseSaldo saldo = null;
        final List<ExtratoResponseTransacao> ultimasTransacoes = new ArrayList<>();
        for (Map<String, Object> row : result) {
            if (saldo == null) {
                saldo = new ExtratoResponseSaldo(
                        (Long) row.get("saldo"),
                        OffsetDateTime.now(ZoneOffset.UTC),
                        (Long) row.get("limite")
                );
            }
            ultimasTransacoes.add(
                    new ExtratoResponseTransacao(
                            (Long) row.get("valor"),
                            row.get("tipo").toString(),
                            row.get("descricao").toString(),
                            ((Timestamp) row.get("realizada_em")).toInstant().atOffset(ZoneOffset.UTC)
                    )
            );
        }
        return new ExtratoResponse(saldo, ultimasTransacoes);
    }

    private Long valorTransacaoParaSomaSaldo(TransacaoInsertRequest transacaoInsertRequest) {
        return "c".equals(transacaoInsertRequest.tipo()) ?
                transacaoInsertRequest.valor() : -transacaoInsertRequest.valor();
    }
}
