package com.example.demo.service;

import com.example.demo.entity.Categoria;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria salvar(Categoria categoria) {
        // Aqui futuramente podemos adicionar regras de negócio,
        // como validar se o nome já existe antes de salvar, etc.
        return categoriaRepository.save(categoria);
    }

     /** READ (by ID)
     * Busca uma categoria pelo seu ID. Retorna um Optional, que é uma forma
     * segura de lidar com a possibilidade do objeto não ser encontrado.
     */
    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    /**
     * UPDATE
     * Atualiza uma categoria. Verifica se a categoria com o ID fornecido existe.
     * Se existir, atualiza os dados e salva.
     */
    public Categoria atualizar(Integer id, Categoria categoriaDetalhes) {
        // Busca a categoria no banco para garantir que ela existe
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o id: " + id));

        // Atualiza os campos do objeto existente com os novos dados
        categoriaExistente.setNome(categoriaDetalhes.getNome());
        categoriaExistente.setDescricao(categoriaDetalhes.getDescricao());

        // Salva a categoria atualizada. O JPA entende que é um UPDATE porque o objeto já tem um ID.
        return categoriaRepository.save(categoriaExistente);
    }

    /**
     * DELETE
     * Deleta uma categoria pelo seu ID.
     */
    public void deletar(Integer id) {
        // O método deleteById já verifica se existe e lança uma exceção se não encontrar.
        // Ou podemos verificar antes para um controle maior.
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada com o id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}