package br.com.fiap.vaultbank.dto;

import br.com.fiap.vaultbank.model.Transacao;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransacaoRequest {

    @NotNull(message = "Tipo de transação é obrigatório")
    private Transacao.TipoTransacao tipo;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
    private BigDecimal valor;

    @Size(max = 255)
    private String descricao;
}
