package br.com.fiap.vaultbank.dto;

import br.com.fiap.vaultbank.model.Transacao;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransacaoResponse {
    private Long id;
    private Transacao.TipoTransacao tipo;
    private BigDecimal valor;
    private String descricao;
    private LocalDateTime realizadaEm;
    private Long contaId;
    private String titularConta;

    public static TransacaoResponse fromEntity(Transacao t) {
        return TransacaoResponse.builder()
                .id(t.getId())
                .tipo(t.getTipo())
                .valor(t.getValor())
                .descricao(t.getDescricao())
                .realizadaEm(t.getRealizadaEm())
                .contaId(t.getConta().getId())
                .titularConta(t.getConta().getTitular())
                .build();
    }
}
