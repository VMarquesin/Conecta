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
import org.springframework.security.core.context.SecurityContextHolder; // Este é usado

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
public Publicacao criar(Integer prestadorId, PublicacaoDTO publicacaoDTO) {
    
    // --- INÍCIO DA CORREÇÃO DE SEGURANÇA ---

    // 1. Pegue o email do usuário logado (do token JWT)
    String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

    // 2. Busque o Prestador LOGADO
    Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new UsernameNotFoundException("Prestador não encontrado com o email: " + emailUsuarioLogado));
    
    // 3. VERIFICAÇÃO DE DONO (OPCIONAL, MAS RECOMENDADA)
    // O usuário logado está tentando postar na sua própria página de perfil?
    // (O prestadorId da URL deve ser o mesmo do prestador logado)
    if (!prestadorLogado.getId().equals(prestadorId)) {
        throw new AccessDeniedException("Você não pode criar uma publicação no perfil de outro prestador.");
    }
    // --- FIM DA CORREÇÃO DE SEGURANÇA ---

    Publicacao publicacao = new Publicacao();
    publicacao.setTitulo(publicacaoDTO.getTitulo());
    publicacao.setDescricao(publicacaoDTO.getDescricao());
    publicacao.setFotoUrl(publicacaoDTO.getFotoUrl());
    
    // 4. Associa a publicação ao PRESTADOR LOGADO (e não ao ID da URL)
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

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailUsuarioLogado;

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            emailUsuarioLogado = userDetails.getUsername();
        } else {
            emailUsuarioLogado = principal.toString(); // <-- aqui pega o "sub" do JWT
        }

        System.out.println("EMAIL DO TOKEN: " + emailUsuarioLogado);

        Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicação não encontrada: " + publicacaoId));

        System.out.println("EMAIL DO DONO DA PUBLICAÇÃO: " + publicacao.getPrestador().getEmail());

        if (!publicacao.getPrestador().getEmail().equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Você não tem permissão para deletar esta publicação.");
        }

        publicacaoRepository.delete(publicacao);
    }
}