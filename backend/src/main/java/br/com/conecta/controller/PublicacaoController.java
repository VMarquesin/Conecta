package br.com.conecta.controller;

import br.com.conecta.dto.PublicacaoDTO;
import br.com.conecta.entity.Publicacao;
import br.com.conecta.service.PublicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestadores/{prestadorId}/publicacoes")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;

    @PostMapping
    public ResponseEntity<Publicacao> criar(@PathVariable Integer prestadorId, @RequestBody PublicacaoDTO publicacaoDTO) {
        try {
            Publicacao novaPublicacao = publicacaoService.criar(prestadorId, publicacaoDTO);
            return ResponseEntity.ok(novaPublicacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Publicacao>> listarPorPrestador(@PathVariable Integer prestadorId) {
        List<Publicacao> publicacoes = publicacaoService.listarPorPrestador(prestadorId);
        return ResponseEntity.ok(publicacoes);
    }

    @PutMapping("/{publicacaoId}")
    public ResponseEntity<Publicacao> atualizar(@PathVariable Integer publicacaoId, @RequestBody PublicacaoDTO publicacaoDTO) {
        // A lógica de segurança está toda no Service
        Publicacao publicacaoAtualizada = publicacaoService.atualizar(publicacaoId, publicacaoDTO);
        return ResponseEntity.ok(publicacaoAtualizada);
    }

    @DeleteMapping("/{publicacaoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer publicacaoId) {
        publicacaoService.deletar(publicacaoId);
        return ResponseEntity.noContent().build();
    }
}