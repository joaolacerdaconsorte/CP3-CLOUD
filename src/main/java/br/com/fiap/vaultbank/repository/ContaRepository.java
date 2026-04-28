package br.com.fiap.vaultbank.repository;

import br.com.fiap.vaultbank.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByCpf(String cpf);

    Optional<Conta> findByNumeroConta(String numeroConta);

    Optional<Conta> findByEmail(String email);

    List<Conta> findByTipo(Conta.TipoConta tipo);

    List<Conta> findByAtiva(Boolean ativa);

    List<Conta> findByTitularContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
