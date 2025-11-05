package br.com.conecta.controller;

import br.com.conecta.dto.AvaliacaoDTO;
import br.com.conecta.entity.Avaliacao;
import br.com.conecta.service.AvaliacaoService;
import br.com.conecta.dto.AvaliacaoResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestadores/{prestadorId}/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@PathVariable Integer prestadorId, @RequestBody AvaliacaoDTO avaliacaoDTO) {
        try {
            Avaliacao novaAvaliacao = avaliacaoService.criar(prestadorId, avaliacaoDTO);
            return ResponseEntity.ok(novaAvaliacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorPrestador(@PathVariable Integer prestadorId) {
        List<AvaliacaoResponseDTO> avaliacoesDTO = avaliacaoService.listarPorPrestador(prestadorId);
        return ResponseEntity.ok(avaliacoesDTO);
    }

    @PutMapping("/{avaliacaoId}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable Integer prestadorId,
                                               @PathVariable Integer avaliacaoId,
                                               @RequestBody AvaliacaoDTO avaliacaoDTO) {
        try {
            Avaliacao avaliacaoAtualizada = avaliacaoService.atualizar(prestadorId, avaliacaoId, avaliacaoDTO);
            return ResponseEntity.ok(avaliacaoAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer prestadorId,
                                        @PathVariable Integer avaliacaoId,
                                        @RequestParam Integer clienteId) {
        try {
            avaliacaoService.deletar(prestadorId, avaliacaoId, clienteId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }    
}