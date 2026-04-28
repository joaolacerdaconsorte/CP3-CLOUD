package br.com.fiap.vaultbank.controller;

import br.com.fiap.vaultbank.dto.*;
import br.com.fiap.vaultbank.model.Conta;
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
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "CRUD completo de contas bancárias")
@CrossOrigin(origins = "*")
public class ContaController {

    private final VaultBankService service;

    @GetMapping
    @Operation(summary = "Listar todas as contas")
    public ResponseEntity<List<ContaResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarContas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID")
    public ResponseEntity<ContaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarConta(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar contas por nome do titular")
    public ResponseEntity<List<ContaResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar contas por tipo (CORRENTE, POUPANCA, INVESTIMENTO)")
    public ResponseEntity<List<ContaResponse>> buscarPorTipo(@PathVariable Conta.TipoConta tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @PostMapping
    @Operation(summary = "Criar nova conta bancária")
    public ResponseEntity<ContaResponse> criar(@Valid @RequestBody ContaRequest request) {
        ContaResponse response = service.criarConta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados da conta")
    public ResponseEntity<ContaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContaRequest request) {
        return ResponseEntity.ok(service.atualizarConta(id, request));
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar conta (soft delete)")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativarConta(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar conta permanentemente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarConta(id);
        return ResponseEntity.noContent().build();
    }
}
