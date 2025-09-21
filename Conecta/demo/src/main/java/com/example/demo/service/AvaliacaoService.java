package com.example.demo.service;

import com.example.demo.dto.AvaliacaoDTO;
import com.example.demo.entity.Avaliacao;
import com.example.demo.entity.Cliente;

import com.example.demo.entity.Prestador;
import com.example.demo.repository.AvaliacaoRepository;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.PrestadorRepository;
import com.example.demo.exception.ResourceNotFoundException; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AvaliacaoResponseDTO;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    @Autowired
    private PrestadorRepository prestadorRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Avaliacao criar(Integer prestadorId, AvaliacaoDTO avaliacaoDTO) {
        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));
        Cliente cliente = clienteRepository.findById(avaliacaoDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());
        avaliacao.setPrestador(prestador);
        avaliacao.setCliente(cliente);

        return avaliacaoRepository.save(avaliacao);
    }
    @Transactional
    public Avaliacao atualizar(Integer prestadorId, Integer avaliacaoId, AvaliacaoDTO avaliacaoDTO) {
        // Valida se o prestador e o cliente existem
        prestadorRepository.findById(prestadorId).orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));
        clienteRepository.findById(avaliacaoDTO.getClienteId()).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));
        
        // Validações de segurança
        if (!avaliacao.getPrestador().getId().equals(prestadorId)) {
            throw new ResourceNotFoundException("Avaliação não pertence a este prestador.");
        }
        if (!avaliacao.getCliente().getId().equals(avaliacaoDTO.getClienteId())) {
            throw new ResourceNotFoundException("Apenas o cliente que criou a avaliação pode editá-la.");
        }

        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());

        return avaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public void deletar(Integer prestadorId, Integer avaliacaoId, Integer clienteId) {
        // Valida se o prestador existe
        prestadorRepository.findById(prestadorId).orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));
        
        // Validações de segurança
        if (!avaliacao.getPrestador().getId().equals(prestadorId)) {
            throw new RuntimeException("Avaliação não pertence a este prestador.");
        }
        if (!avaliacao.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("Apenas o cliente que criou a avaliação pode deletá-la.");
        }

        avaliacaoRepository.deleteById(avaliacaoId);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorPrestador(Integer prestadorId) {
        // 1. Busca as entidades do banco
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByPrestadorId(prestadorId);

        // 2. Converte a lista de Entidades para uma lista de DTOs
        // A conversão acontece aqui, com a transação ainda aberta.
        return avaliacoes.stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
    }
}