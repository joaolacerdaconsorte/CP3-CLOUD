package br.com.fiap.vaultbank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Tag(name = "Status", description = "Health check e informações da API")
@CrossOrigin(origins = "*")
public class StatusController {

    @GetMapping("/")
    @Operation(summary = "Página inicial da API")
    public Map<String, Object> home() {
        return Map.of(
            "api", "VaultBank API",
            "versao", "1.0.0",
            "descricao", "API REST bancária fictícia — CP2 DevOps | FIAP 2TDSPW 2026",
            "autor", "João Vitor Lacerda Consorte",
            "documentacao", "/swagger-ui.html",
            "status", "ONLINE",
            "timestamp", LocalDateTime.now().toString()
        );
    }

    @GetMapping("/api/health")
    @Operation(summary = "Health check")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "database", "CONNECTED",
            "timestamp", LocalDateTime.now().toString()
        );
    }
}
