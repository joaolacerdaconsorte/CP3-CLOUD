package br.com.fiap.vaultbank.controller;

import br.com.fiap.vaultbank.dto.*;
import br.com.fiap.vaultbank.service.VaultBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas/{contaId}/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "CRUD de transações bancárias vinculadas a contas")
@CrossOrigin(origins = "*")
public class TransacaoController {

    private final VaultBankService service;

    @GetMapping
    @Operation(summary = "Listar transações de uma conta")
    public ResponseEntity<List<TransacaoResponse>> listar(@PathVariable Long contaId) {
        return ResponseEntity.ok(service.listarTransacoesDaConta(contaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID")
    public ResponseEntity<TransacaoResponse> buscar(@PathVariable Long contaId, @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarTransacao(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova transação (DEPOSITO, SAQUE, PIX, PAGAMENTO, TRANSFERENCIA)")
    public ResponseEntity<TransacaoResponse> criar(
            @PathVariable Long contaId,
            @Valid @RequestBody TransacaoRequest request) {
        TransacaoResponse response = service.criarTransacao(contaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar transação")
    public ResponseEntity<Void> deletar(@PathVariable Long contaId, @PathVariable Long id) {
        service.deletarTransacao(id);
        return ResponseEntity.noContent().build();
    }
}
