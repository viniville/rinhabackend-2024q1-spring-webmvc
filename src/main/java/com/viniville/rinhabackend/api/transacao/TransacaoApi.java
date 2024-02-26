package com.viniville.rinhabackend.api.transacao;

import com.viniville.rinhabackend.repository.TransacaoRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class TransacaoApi {

    private final TransacaoRepository transacaoRepository;

    public TransacaoApi(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @PostMapping("/{id}/transacoes")
    public TransacaoInsertResponse insere(@PathVariable("id") Long idCliente, @Valid @RequestBody TransacaoInsertRequest transacaoInsertRequest) {
        return transacaoRepository.registrarTransacao(transacaoInsertRequest.withIdCliente(idCliente));
    }

    @GetMapping("/{id}/extrato")
    public ExtratoResponse extrato(@PathVariable("id") Long idCliente) {
        return transacaoRepository.extrato(idCliente);
    }

}
