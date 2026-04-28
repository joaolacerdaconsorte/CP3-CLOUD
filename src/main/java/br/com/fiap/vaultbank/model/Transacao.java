package br.com.fiap.vaultbank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tipo de transação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTransacao tipo;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Size(max = 255, message = "Descrição pode ter no máximo 255 caracteres")
    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime realizadaEm = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false)
    @JsonBackReference
    private Conta conta;

    public enum TipoTransacao {
        DEPOSITO, SAQUE, TRANSFERENCIA, PIX, PAGAMENTO
    }
}
