package br.com.conecta.controller;

import br.com.conecta.entity.Categoria;
import br.com.conecta.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> listarTodas() {
        return categoriaService.listarTodas();
    }

    @PostMapping
    public ResponseEntity<Categoria> salvar(@RequestBody Categoria categoria) {
        Categoria categoriaSalva = categoriaService.salvar(categoria);
        return ResponseEntity.ok(categoriaSalva);
    }

    // --- NOVOS ENDPOINTS ABAIXO ---

    /**
     * READ (by ID)
     * Ex: GET http://localhost:8080/api/categorias/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
        return categoriaService.buscarPorId(id)
                .map(categoria -> ResponseEntity.ok(categoria)) // Se encontrar, retorna 200 OK com o objeto
                .orElse(ResponseEntity.notFound().build());    // Se não encontrar, retorna 404 Not Found
    }

    /**
     * UPDATE
     * Ex: PUT http://localhost:8080/api/categorias/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Integer id, @RequestBody Categoria categoriaDetalhes) {
        try {
            Categoria categoriaAtualizada = categoriaService.atualizar(id, categoriaDetalhes);
            return ResponseEntity.ok(categoriaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE
     * Ex: DELETE http://localhost:8080/api/categorias/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        try {
            categoriaService.deletar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content, indicando sucesso sem conteúdo.
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}