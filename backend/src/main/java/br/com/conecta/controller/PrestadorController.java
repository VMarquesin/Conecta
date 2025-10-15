package br.com.conecta.controller;

import br.com.conecta.dto.PrestadorDTO;
import br.com.conecta.entity.Prestador;
import br.com.conecta.service.PrestadorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.conecta.entity.Telefone;
import br.com.conecta.dto.TelefoneDTO;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prestadores")
public class PrestadorController {

    @Autowired
    private PrestadorService prestadorService;

    @GetMapping
    public List<Prestador> listarTodos() {
        return prestadorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestador> buscarPorId(@PathVariable Integer id) {
        return prestadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prestador> salvar(@RequestBody PrestadorDTO prestadorDTO) {
        Prestador prestadorSalvo = prestadorService.salvar(prestadorDTO);
        return ResponseEntity.ok(prestadorSalvo);
    }

       @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        prestadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prestador> atualizar(@PathVariable Integer id, @Valid @RequestBody PrestadorDTO prestadorDTO) {
        // A exceção ResourceNotFoundException será capturada pelo nosso GlobalExceptionHandler
        Prestador prestadorAtualizado = prestadorService.atualizar(id, prestadorDTO);
        return ResponseEntity.ok(prestadorAtualizado);
    }
    
    // Endpoint para associar uma categoria a um prestador
    @PostMapping("/{prestadorId}/telefones")
    public ResponseEntity<Prestador> adicionarTelefone(@PathVariable Integer prestadorId, @RequestBody TelefoneDTO telefoneDTO) {
        try {
            Prestador prestadorAtualizado = prestadorService.adicionarTelefone(prestadorId, telefoneDTO);
            return ResponseEntity.ok(prestadorAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{prestadorId}/telefones/{telefoneId}")
    public ResponseEntity<Telefone> atualizarTelefone(@PathVariable Integer prestadorId,
                                                      @PathVariable Integer telefoneId,
                                                      @RequestBody TelefoneDTO telefoneDTO) {
        try {
            Telefone telefoneAtualizado = prestadorService.atualizarTelefone(prestadorId, telefoneId, telefoneDTO);
            return ResponseEntity.ok(telefoneAtualizado);
        } catch (RuntimeException e) {
            // Retorna notFound para IDs não encontrados ou badRequest para a validação de segurança
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{prestadorId}/telefones/{telefoneId}")
    public ResponseEntity<Void> deletarTelefone(
        @PathVariable Integer prestadorId,
        @PathVariable Integer telefoneId) {
        try {
            prestadorService.deletarTelefone(prestadorId, telefoneId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/{prestadorId}/categorias/{categoriaId}")
    public ResponseEntity<Prestador> adicionarCategoria(
            @PathVariable Integer prestadorId,
            @PathVariable Integer categoriaId) {
        try {
            Prestador prestadorAtualizado = prestadorService.adicionarCategoria(prestadorId, categoriaId);
            return ResponseEntity.ok(prestadorAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}