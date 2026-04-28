package br.com.fiap.vaultbank.config;

import br.com.fiap.vaultbank.model.Conta;
import br.com.fiap.vaultbank.model.Transacao;
import br.com.fiap.vaultbank.repository.ContaRepository;
import br.com.fiap.vaultbank.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    @Override
    public void run(String... args) {
        if (contaRepository.count() > 0) {
            log.info("✅ Dados já existentes — seed ignorado.");
            return;
        }

        log.info("🏦 Inicializando dados fictícios do VaultBank...");

        // ---- Conta 1: João Vitor ----
        Conta joao = contaRepository.save(Conta.builder()
                .titular("João Vitor Lacerda Consorte")
                .cpf("12345678901")
                .email("joao.consorte@fiap.com.br")
                .tipo(Conta.TipoConta.CORRENTE)
                .numeroConta("0001-00000001-0")
                .saldo(new BigDecimal("15750.00"))
                .ativa(true)
                .criadaEm(LocalDateTime.now().minusDays(90))
                .atualizadaEm(LocalDateTime.now())
                .build());

        // ---- Conta 2: Maria Silva ----
        Conta maria = contaRepository.save(Conta.builder()
                .titular("Maria Silva")
                .cpf("98765432100")
                .email("maria.silva@email.com")
                .tipo(Conta.TipoConta.POUPANCA)
                .numeroConta("0001-00000002-5")
                .saldo(new BigDecimal("42300.50"))
                .ativa(true)
                .criadaEm(LocalDateTime.now().minusDays(180))
                .atualizadaEm(LocalDateTime.now())
                .build());

        // ---- Conta 3: Carlos Oliveira ----
        Conta carlos = contaRepository.save(Conta.builder()
                .titular("Carlos Oliveira")
                .cpf("11122233344")
                .email("carlos.oliveira@email.com")
                .tipo(Conta.TipoConta.INVESTIMENTO)
                .numeroConta("0001-00000003-1")
                .saldo(new BigDecimal("125000.00"))
                .ativa(true)
                .criadaEm(LocalDateTime.now().minusDays(365))
                .atualizadaEm(LocalDateTime.now())
                .build());

        // ---- Conta 4: Ana Beatriz (desativada) ----
        contaRepository.save(Conta.builder()
                .titular("Ana Beatriz Ferreira")
                .cpf("55566677788")
                .email("ana.ferreira@email.com")
                .tipo(Conta.TipoConta.CORRENTE)
                .numeroConta("0001-00000004-8")
                .saldo(new BigDecimal("0.00"))
                .ativa(false)
                .criadaEm(LocalDateTime.now().minusDays(400))
                .atualizadaEm(LocalDateTime.now().minusDays(30))
                .build());

        // ---- Conta 5: Pedro Santos ----
        Conta pedro = contaRepository.save(Conta.builder()
                .titular("Pedro Santos")
                .cpf("99988877766")
                .email("pedro.santos@email.com")
                .tipo(Conta.TipoConta.CORRENTE)
                .numeroConta("0001-00000005-3")
                .saldo(new BigDecimal("8920.75"))
                .ativa(true)
                .criadaEm(LocalDateTime.now().minusDays(60))
                .atualizadaEm(LocalDateTime.now())
                .build());

        // ---- Transações do João ----
        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.DEPOSITO)
                .valor(new BigDecimal("5000.00"))
                .descricao("Salário mensal")
                .realizadaEm(LocalDateTime.now().minusDays(5))
                .conta(joao).build());

        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.PIX)
                .valor(new BigDecimal("250.00"))
                .descricao("PIX para restaurante")
                .realizadaEm(LocalDateTime.now().minusDays(3))
                .conta(joao).build());

        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.PAGAMENTO)
                .valor(new BigDecimal("1200.00"))
                .descricao("Pagamento de aluguel")
                .realizadaEm(LocalDateTime.now().minusDays(1))
                .conta(joao).build());

        // ---- Transações da Maria ----
        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.DEPOSITO)
                .valor(new BigDecimal("10000.00"))
                .descricao("Depósito de poupança")
                .realizadaEm(LocalDateTime.now().minusDays(15))
                .conta(maria).build());

        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.SAQUE)
                .valor(new BigDecimal("500.00"))
                .descricao("Saque em caixa eletrônico")
                .realizadaEm(LocalDateTime.now().minusDays(2))
                .conta(maria).build());

        // ---- Transações do Carlos ----
        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.DEPOSITO)
                .valor(new BigDecimal("50000.00"))
                .descricao("Aporte em investimento")
                .realizadaEm(LocalDateTime.now().minusDays(30))
                .conta(carlos).build());

        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.TRANSFERENCIA)
                .valor(new BigDecimal("2500.00"))
                .descricao("Transferência para conta corrente")
                .realizadaEm(LocalDateTime.now().minusDays(7))
                .conta(carlos).build());

        // ---- Transações do Pedro ----
        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.PIX)
                .valor(new BigDecimal("89.90"))
                .descricao("PIX para iFood")
                .realizadaEm(LocalDateTime.now().minusHours(6))
                .conta(pedro).build());

        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.PAGAMENTO)
                .valor(new BigDecimal("350.00"))
                .descricao("Conta de energia")
                .realizadaEm(LocalDateTime.now().minusDays(10))
                .conta(pedro).build());

        transacaoRepository.save(Transacao.builder()
                .tipo(Transacao.TipoTransacao.DEPOSITO)
                .valor(new BigDecimal("3500.00"))
                .descricao("Freelance de desenvolvimento")
                .realizadaEm(LocalDateTime.now().minusDays(20))
                .conta(pedro).build());

        log.info("✅ {} contas e {} transações criadas com sucesso!",
                contaRepository.count(), transacaoRepository.count());
    }
}
