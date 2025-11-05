package br.com.conecta.repository;

import br.com.conecta.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}