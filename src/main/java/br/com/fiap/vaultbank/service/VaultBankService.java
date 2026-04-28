package br.com.fiap.vaultbank.service;

import br.com.fiap.vaultbank.dto.*;
import br.com.fiap.vaultbank.exception.ResourceNotFoundException;
import br.com.fiap.vaultbank.exception.BusinessException;
import br.com.fiap.vaultbank.model.Conta;
import br.com.fiap.vaultbank.model.Transacao;
import br.com.fiap.vaultbank.repository.ContaRepository;
import br.com.fiap.vaultbank.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaultBankService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    // ==================== CONTAS ====================

    @Transactional(readOnly = true)
    public List<ContaResponse> listarContas() {
        return contaRepository.findAll().stream()
                .map(ContaResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarConta(Long id) {
        return ContaResponse.fromEntity(findContaOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarPorNome(String nome) {
        return contaRepository.findByTitularContainingIgnoreCase(nome).stream()
                .map(ContaResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarPorTipo(Conta.TipoConta tipo) {
        return contaRepository.findByTipo(tipo).stream()
                .map(ContaResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ContaResponse criarConta(ContaRequest request) {
        if (contaRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe uma conta com o CPF informado");
        }
        if (contaRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe uma conta com o e-mail informado");
        }

        Conta conta = Conta.builder()
                .titular(request.getTitular())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .tipo(request.getTipo())
                .numeroConta(gerarNumeroConta())
                .saldo(request.getSaldoInicial() != null ? request.getSaldoInicial() : BigDecimal.ZERO)
                .ativa(true)
                .criadaEm(LocalDateTime.now())
                .atualizadaEm(LocalDateTime.now())
                .build();

        return ContaResponse.fromEntity(contaRepository.save(conta));
    }

    @Transactional
    public ContaResponse atualizarConta(Long id, ContaRequest request) {
        Conta conta = findContaOrThrow(id);

        // Verificar duplicidade de CPF (se mudou)
        if (!conta.getCpf().equals(request.getCpf()) && contaRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe outra conta com o CPF informado");
        }
        // Verificar duplicidade de email (se mudou)
        if (!conta.getEmail().equals(request.getEmail()) && contaRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe outra conta com o e-mail informado");
        }

        conta.setTitular(request.getTitular());
        conta.setCpf(request.getCpf());
        conta.setEmail(request.getEmail());
        conta.setTipo(request.getTipo());

        return ContaResponse.fromEntity(contaRepository.save(conta));
    }

    @Transactional
    public void desativarConta(Long id) {
        Conta conta = findContaOrThrow(id);
        conta.setAtiva(false);
        contaRepository.save(conta);
    }

    @Transactional
    public void deletarConta(Long id) {
        Conta conta = findContaOrThrow(id);
        contaRepository.delete(conta);
    }

    // ==================== TRANSAÇÕES ====================

    @Transactional(readOnly = true)
    public List<TransacaoResponse> listarTransacoesDaConta(Long contaId) {
        findContaOrThrow(contaId);
        return transacaoRepository.findByContaIdOrderByRealizadaEmDesc(contaId).stream()
                .map(TransacaoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransacaoResponse buscarTransacao(Long id) {
        Transacao t = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada com ID: " + id));
        return TransacaoResponse.fromEntity(t);
    }

    @Transactional
    public TransacaoResponse criarTransacao(Long contaId, TransacaoRequest request) {
        Conta conta = findContaOrThrow(contaId);

        if (!conta.getAtiva()) {
            throw new BusinessException("Conta desativada. Não é possível realizar transações.");
        }

        // Atualizar saldo conforme o tipo
        switch (request.getTipo()) {
            case DEPOSITO -> conta.setSaldo(conta.getSaldo().add(request.getValor()));
            case SAQUE, PAGAMENTO, PIX -> {
                if (conta.getSaldo().compareTo(request.getValor()) < 0) {
                    throw new BusinessException("Saldo insuficiente. Saldo atual: R$ " + conta.getSaldo());
                }
                conta.setSaldo(conta.getSaldo().subtract(request.getValor()));
            }
            case TRANSFERENCIA -> {
                if (conta.getSaldo().compareTo(request.getValor()) < 0) {
                    throw new BusinessException("Saldo insuficiente para transferência");
                }
                conta.setSaldo(conta.getSaldo().subtract(request.getValor()));
            }
        }

        Transacao transacao = Transacao.builder()
                .tipo(request.getTipo())
                .valor(request.getValor())
                .descricao(request.getDescricao())
                .realizadaEm(LocalDateTime.now())
                .conta(conta)
                .build();

        contaRepository.save(conta);
        return TransacaoResponse.fromEntity(transacaoRepository.save(transacao));
    }

    @Transactional
    public void deletarTransacao(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada com ID: " + id));
        transacaoRepository.delete(transacao);
    }

    // ==================== HELPERS ====================

    private Conta findContaOrThrow(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com ID: " + id));
    }

    private String gerarNumeroConta() {
        Random random = new Random();
        String numero;
        do {
            numero = String.format("%04d-%08d-%d",
                    random.nextInt(9999),
                    random.nextInt(99999999),
                    random.nextInt(9));
        } while (contaRepository.findByNumeroConta(numero).isPresent());
        return numero;
    }
}
