package com.example.demo.controller;

import com.example.demo.dto.PublicacaoDTO;
import com.example.demo.entity.Publicacao;
import com.example.demo.service.PublicacaoService;
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
    public ResponseEntity<Publicacao> atualizar(@PathVariable Integer prestadorId,
                                                @PathVariable Integer publicacaoId,
                                                @RequestBody PublicacaoDTO publicacaoDTO) {
        try {
            Publicacao publicacaoAtualizada = publicacaoService.atualizar(prestadorId, publicacaoId, publicacaoDTO);
            return ResponseEntity.ok(publicacaoAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{publicacaoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer prestadorId,
                                        @PathVariable Integer publicacaoId) {
        try {
            publicacaoService.deletar(prestadorId, publicacaoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}