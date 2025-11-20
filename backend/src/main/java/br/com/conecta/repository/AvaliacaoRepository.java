package br.com.conecta.repository;

import br.com.conecta.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {
    List<Avaliacao> findByPrestadorId(Integer prestadorId);

    boolean existsByPrestadorIdAndClienteEmail(Integer prestadorId, String email);
}