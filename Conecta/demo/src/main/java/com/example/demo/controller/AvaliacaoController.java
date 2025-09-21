package com.example.demo.controller;

import com.example.demo.dto.AvaliacaoDTO;
import com.example.demo.entity.Avaliacao;
import com.example.demo.service.AvaliacaoService;
import com.example.demo.dto.AvaliacaoResponseDTO;

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

    // Para deletar, o cliente precisa se identificar. Uma forma simples é passar o ID dele.
    // Ex: DELETE /api/prestadores/1/avaliacoes/1?clienteId=1
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