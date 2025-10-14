package br.com.conecta.service;

import br.com.conecta.dto.PublicacaoDTO;
import br.com.conecta.entity.Prestador;
import br.com.conecta.entity.Publicacao;

import br.com.conecta.repository.PrestadorRepository;
import br.com.conecta.repository.PublicacaoRepository;
import br.com.conecta.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PublicacaoService {

    @Autowired
    private PublicacaoRepository publicacaoRepository;
    @Autowired
    private PrestadorRepository prestadorRepository;

    @Transactional
    public Publicacao criar(Integer prestadorId, PublicacaoDTO publicacaoDTO) {
        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Publicacao publicacao = new Publicacao();
        publicacao.setTitulo(publicacaoDTO.getTitulo());
        publicacao.setDescricao(publicacaoDTO.getDescricao());
        publicacao.setFotoUrl(publicacaoDTO.getFotoUrl());
        publicacao.setPrestador(prestador);

        return publicacaoRepository.save(publicacao);
    }

    @Transactional
    public Publicacao atualizar(Integer prestadorId, Integer publicacaoId, PublicacaoDTO publicacaoDTO) {
        // Valida se o prestador existe
        prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicação não encontrada"));
        
        // Valida se a publicação pertence ao prestador correto
        if (!publicacao.getPrestador().getId().equals(prestadorId)) {
            throw new ResourceNotFoundException("Esta publicação não pertence ao prestador informado.");
        }

        publicacao.setTitulo(publicacaoDTO.getTitulo());
        publicacao.setDescricao(publicacaoDTO.getDescricao());
        publicacao.setFotoUrl(publicacaoDTO.getFotoUrl());

        return publicacaoRepository.save(publicacao);
    }

    @Transactional
    public void deletar(Integer prestadorId, Integer publicacaoId) {
        // Valida se o prestador existe
        prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicação não encontrada"));
        
        // Valida se a publicação pertence ao prestador correto
        if (!publicacao.getPrestador().getId().equals(prestadorId)) {
            throw new ResourceNotFoundException("Esta publicação não pertence ao prestador informado.");
        }
        
        publicacaoRepository.deleteById(publicacaoId);
    }

    @Transactional(readOnly = true)
    public List<Publicacao> listarPorPrestador(Integer prestadorId) {
        return publicacaoRepository.findByPrestadorId(prestadorId);
    }
}