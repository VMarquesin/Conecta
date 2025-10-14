package br.com.conecta.repository;

import br.com.conecta.entity.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacaoRepository extends JpaRepository<Publicacao, Integer> {
    List<Publicacao> findByPrestadorId(Integer prestadorId);
}