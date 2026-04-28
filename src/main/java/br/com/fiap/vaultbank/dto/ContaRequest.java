package br.com.fiap.vaultbank.dto;

import br.com.fiap.vaultbank.model.Conta;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ContaRequest {

    @NotBlank(message = "Nome do titular é obrigatório")
    @Size(min = 2, max = 120)
    private String titular;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "E-mail é obrigatório")
    @Email
    private String email;

    @NotNull(message = "Tipo de conta é obrigatório")
    private Conta.TipoConta tipo;

    private BigDecimal saldoInicial;
}
