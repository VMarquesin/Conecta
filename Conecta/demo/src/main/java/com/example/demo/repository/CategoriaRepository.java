package com.example.demo.repository;

import com.example.demo.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Boa prática indicar que é um componente de repositório
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // JpaRepository<Entidade, TipoDaChavePrimaria>
}