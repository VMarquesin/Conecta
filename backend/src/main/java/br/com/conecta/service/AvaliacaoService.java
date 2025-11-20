package br.com.conecta.service;

import br.com.conecta.dto.AvaliacaoDTO;
import br.com.conecta.entity.Avaliacao;
import br.com.conecta.entity.Cliente;

import br.com.conecta.entity.Prestador;
import br.com.conecta.entity.Publicacao;
import br.com.conecta.repository.AvaliacaoRepository;
import br.com.conecta.repository.ClienteRepository;
import br.com.conecta.repository.PrestadorRepository;
import br.com.conecta.repository.PublicacaoRepository;
import br.com.conecta.exception.ResourceNotFoundException; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import br.com.conecta.dto.AvaliacaoResponseDTO;
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
    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Transactional
    public Avaliacao criarParaPrestador(Integer prestadorId, AvaliacaoDTO avaliacaoDTO) {
        
        // 2. DESCOBRIR O USUÁRIO LOGADO
        // Pega a autenticação do "contexto de segurança" (que o nosso filtro JWT preencheu)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailDoUsuarioLogado = authentication.getName(); // Isso nos dá o email

        if (avaliacaoRepository.existsByPrestadorIdAndClienteEmail(prestadorId, emailDoUsuarioLogado)){
            throw new RuntimeException("Você já avaliou este prestador. Edite sua avaliação anterior.");
        }
        // 3. BUSCAR O CLIENTE PELO EMAIL
        // (Não podemos mais confiar no clienteId que vem do DTO)
        Cliente cliente = clienteRepository.findByEmail(emailDoUsuarioLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado com o email: " + emailDoUsuarioLogado));

        // --- O resto da lógica é o mesmo ---
        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());
        avaliacao.setPrestador(prestador);
        avaliacao.setCliente(cliente); // 4. Usa o cliente que buscamos pelo token

        return avaliacaoRepository.save(avaliacao);
    }
    
    @Transactional
    public Avaliacao criarParaPublicacao(Integer publicacaoId, AvaliacaoDTO avaliacaoDTO) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String emailDoUsuarioLogado = authentication.getName();
            
            Cliente cliente = clienteRepository.findByEmail(emailDoUsuarioLogado)
                    .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado"));
            
            Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Publicação não encontrada"));
            
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setNota(avaliacaoDTO.getNota());
            avaliacao.setComentario(avaliacaoDTO.getComentario());
            avaliacao.setCliente(cliente);
            
            // VINCULA À PUBLICAÇÃO
            avaliacao.setPublicacao(publicacao); 
            
            // IMPORTANTE: VINCULA TAMBÉM AO PRESTADOR (DONO DA PUBLICAÇÃO)
            // Isso garante que o dado fique consistente no banco
            avaliacao.setPrestador(publicacao.getPrestador()); 
            
            return avaliacaoRepository.save(avaliacao);
    }

    // Dentro da classe AvaliacaoService.java

    @Transactional
    public Avaliacao atualizar(Integer avaliacaoId, AvaliacaoDTO avaliacaoDTO) {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

        // VERIFICAÇÃO DE DONO
        if (!avaliacao.getCliente().getEmail().equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Você não tem permissão para editar esta avaliação.");
        }

        // Se for o dono, atualiza
        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());
        return avaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public void deletar(Integer avaliacaoId) {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

        // VERIFICAÇÃO DE DONO
        if (!avaliacao.getCliente().getEmail().equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Você não tem permissão para deletar esta avaliação.");
        }

        // Se for o dono, deleta
        avaliacaoRepository.delete(avaliacao);
    }
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorPrestador(Integer prestadorId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByPrestadorId(prestadorId);

        // 2. Converte a lista de Entidades para uma lista de DTOs
        // A conversão acontece aqui, com a transação ainda aberta.
        return avaliacoes.stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
    }
}