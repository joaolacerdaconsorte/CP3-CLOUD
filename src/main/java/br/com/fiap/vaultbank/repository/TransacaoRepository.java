package br.com.fiap.vaultbank.repository;

import br.com.fiap.vaultbank.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaIdOrderByRealizadaEmDesc(Long contaId);

    List<Transacao> findByTipo(Transacao.TipoTransacao tipo);

    List<Transacao> findByContaIdAndTipo(Long contaId, Transacao.TipoTransacao tipo);

    List<Transacao> findByRealizadaEmBetween(LocalDateTime inicio, LocalDateTime fim);
}
