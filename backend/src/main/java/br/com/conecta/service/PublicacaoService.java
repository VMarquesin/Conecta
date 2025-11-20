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
import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class PublicacaoService {

    @Autowired
    private PublicacaoRepository publicacaoRepository;
    @Autowired
    private PrestadorRepository prestadorRepository;

    // --- MÉTODO DE CRIAÇÃO ---
    // (Este método está correto)
    @Transactional
public Publicacao criar(PublicacaoDTO publicacaoDTO) { // 1. Removido o 'prestadorId'
    
    // 2. Pega o email do usuário logado (do token JWT)
    String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

    // 3. Busca o Prestador que está logado
    Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new UsernameNotFoundException("Somente um Prestador logado pode criar publicações."));

    Publicacao publicacao = new Publicacao();
    publicacao.setTitulo(publicacaoDTO.getTitulo());
    publicacao.setDescricao(publicacaoDTO.getDescricao());
    publicacao.setFotoUrl(publicacaoDTO.getFotoUrl());
    
    // 4. Associa a publicação ao Prestador DONO DO TOKEN
    publicacao.setPrestador(prestadorLogado);

    return publicacaoRepository.save(publicacao);
}
    // --- MÉTODO DE LEITURA ---
    // (Este método está correto)
    @Transactional(readOnly = true)
    public List<Publicacao> listarPorPrestador(Integer prestadorId) {
        return publicacaoRepository.findByPrestadorId(prestadorId);
    }

    // --- MÉTODOS DE ATUALIZAÇÃO E DELEÇÃO SEGUROS ---
    // (Estes são os métodos corretos que você adicionou)

    @Transactional
    public Publicacao atualizar(Integer publicacaoId, PublicacaoDTO publicacaoDTO) {
        // Pega o email do usuário logado (do token JWT)
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        // Busca a publicação no banco
        Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicação não encontrada: " + publicacaoId));

        // VERIFICAÇÃO DE DONO
        if (!publicacao.getPrestador().getEmail().equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Você não tem permissão para editar esta publicação.");
        }

        // Se for o dono, atualiza os dados
        publicacao.setTitulo(publicacaoDTO.getTitulo());
        publicacao.setDescricao(publicacaoDTO.getDescricao());
        publicacao.setFotoUrl(publicacaoDTO.getFotoUrl());
        return publicacaoRepository.save(publicacao);
    }

    @Transactional
    public void deletar(Integer publicacaoId) {
        
        // 1. ESTA É A FORMA CORRETA de pegar o email do token:
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicação não encontrada: " + publicacaoId));

        // 2. A verificação agora vai funcionar
        if (!publicacao.getPrestador().getEmail().equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Você não tem permissão para deletar esta publicação.");
        }

        publicacaoRepository.delete(publicacao);
    }
}