package br.com.conecta.controller;

import br.com.conecta.dto.PublicacaoDTO;
import br.com.conecta.entity.Publicacao;
import br.com.conecta.service.PublicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/publicacoes")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;

    @PostMapping
    public ResponseEntity<Publicacao> criar(@Valid @RequestBody PublicacaoDTO publicacaoDTO) {
        // A chamada de service agora só passa o DTO
        Publicacao novaPublicacao = publicacaoService.criar(publicacaoDTO);
        return ResponseEntity.ok(novaPublicacao);
    }
    
    // ROTA DE LEITURA (ainda necessária para a página pública)
    @GetMapping("/prestador/{prestadorId}")
    public ResponseEntity<List<Publicacao>> listarPorPrestador(@PathVariable Integer prestadorId) {
        return ResponseEntity.ok(publicacaoService.listarPorPrestador(prestadorId));
    }

    // ROTA DE ATUALIZAR (já estava correta)
    @PutMapping("/{publicacaoId}")
    public ResponseEntity<Publicacao> atualizar(@PathVariable Integer publicacaoId, @RequestBody PublicacaoDTO publicacaoDTO) {
        Publicacao publicacaoAtualizada = publicacaoService.atualizar(publicacaoId, publicacaoDTO);
        return ResponseEntity.ok(publicacaoAtualizada);
    }

    // ROTA DE DELETAR (já estava correta)
    @DeleteMapping("/{publicacaoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer publicacaoId) {
        publicacaoService.deletar(publicacaoId);
        return ResponseEntity.noContent().build();
    }
}