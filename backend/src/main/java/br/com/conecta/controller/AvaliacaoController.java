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
@RequestMapping("/api/avaliacoes") // Rota base correta
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    /**
     * Rota: POST /api/avaliacoes/prestador/{prestadorId}
     */
    @PostMapping("/prestador/{prestadorId}")
    public ResponseEntity<Avaliacao> criarParaPrestador(
            @PathVariable Integer prestadorId, 
            @RequestBody AvaliacaoDTO avaliacaoDTO) {
        
        // Correção: Chamando o método 'criarParaPrestador' (como no seu service)
        Avaliacao novaAvaliacao = avaliacaoService.criarParaPrestador(prestadorId, avaliacaoDTO);
        return ResponseEntity.ok(novaAvaliacao);
    }

    /**
     * Rota: POST /api/avaliacoes/publicacao/{publicacaoId}
     */
     @PostMapping("/publicacao/{publicacaoId}")
    public ResponseEntity<Avaliacao> criarParaPublicacao(
            @PathVariable Integer publicacaoId,
            @RequestBody AvaliacaoDTO avaliacaoDTO) {
        Avaliacao novaAvaliacao = avaliacaoService.criarParaPublicacao(publicacaoId, avaliacaoDTO);
        return ResponseEntity.ok(novaAvaliacao);
    }

    /**
     * Rota: GET /api/avaliacoes/prestador/{prestadorId}
     */
    @GetMapping("/prestador/{prestadorId}") 
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorPrestador(@PathVariable Integer prestadorId) {
        List<AvaliacaoResponseDTO> avaliacoesDTO = avaliacaoService.listarPorPrestador(prestadorId);
        return ResponseEntity.ok(avaliacoesDTO);
    }

    /**
     * Rota: PUT /api/avaliacoes/prestador/{prestadorId}/{avaliacaoId}
     */
    // CORREÇÃO: A rota agora precisa do prestadorId
    // Dentro da classe AvaliacaoController.java

    @PutMapping("/{avaliacaoId}")
    public ResponseEntity<Avaliacao> atualizar(
            @PathVariable Integer avaliacaoId,
            @RequestBody AvaliacaoDTO avaliacaoDTO) {

        Avaliacao avaliacaoAtualizada = avaliacaoService.atualizar(avaliacaoId, avaliacaoDTO);
        return ResponseEntity.ok(avaliacaoAtualizada);
    }

    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer avaliacaoId) { 

        avaliacaoService.deletar(avaliacaoId);
        return ResponseEntity.noContent().build();
    } 
}