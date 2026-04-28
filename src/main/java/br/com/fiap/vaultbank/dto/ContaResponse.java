package br.com.fiap.vaultbank.dto;

import br.com.fiap.vaultbank.model.Conta;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ContaResponse {
    private Long id;
    private String titular;
    private String cpf;
    private String email;
    private Conta.TipoConta tipo;
    private String numeroConta;
    private BigDecimal saldo;
    private Boolean ativa;
    private LocalDateTime criadaEm;
    private LocalDateTime atualizadaEm;
    private int totalTransacoes;

    public static ContaResponse fromEntity(Conta conta) {
        return ContaResponse.builder()
                .id(conta.getId())
                .titular(conta.getTitular())
                .cpf(conta.getCpf())
                .email(conta.getEmail())
                .tipo(conta.getTipo())
                .numeroConta(conta.getNumeroConta())
                .saldo(conta.getSaldo())
                .ativa(conta.getAtiva())
                .criadaEm(conta.getCriadaEm())
                .atualizadaEm(conta.getAtualizadaEm())
                .totalTransacoes(conta.getTransacoes() != null ? conta.getTransacoes().size() : 0)
                .build();
    }
}
