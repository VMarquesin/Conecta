package br.com.conecta.service;

import br.com.conecta.dto.PrestadorDTO;
import br.com.conecta.dto.TelefoneDTO;
import br.com.conecta.entity.Categoria;
import br.com.conecta.entity.Prestador;
import br.com.conecta.entity.Telefone;
import br.com.conecta.exception.ResourceNotFoundException; 
import br.com.conecta.repository.CategoriaRepository;
import br.com.conecta.repository.PrestadorRepository;
import br.com.conecta.repository.TelefoneRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; // Adicione esta importação

@Service
public class PrestadorService {

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Prestador> listarTodos() {
        return prestadorRepository.findAll();
    }

    // --- MÉTODOS FALTANTES PARA PRESTADOR ---
    
    public Optional<Prestador> buscarPorId(Integer id) {
        return prestadorRepository.findById(id);
    }
    
    @Transactional
    public Prestador atualizar(Integer id, PrestadorDTO prestadorDTO) {
        Prestador prestadorExistente = prestadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado com id: " + id));

        prestadorExistente.setNomeCompleto(prestadorDTO.getNomeCompleto());
        prestadorExistente.setNomeFantasia(prestadorDTO.getNomeFantasia());
        prestadorExistente.setEmail(prestadorDTO.getEmail());
        prestadorExistente.setBio(prestadorDTO.getBio());
        prestadorExistente.setFotoPerfilUrl(prestadorDTO.getFotoPerfilUrl());
        prestadorExistente.setCpf(prestadorDTO.getCpf());
        
        if (prestadorDTO.getSenha() != null && !prestadorDTO.getSenha().isEmpty()) {
            prestadorExistente.setSenhaHash(prestadorDTO.getSenha());
        }

        return prestadorRepository.save(prestadorExistente);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!prestadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prestador não encontrado com id: " + id);
        }
        prestadorRepository.deleteById(id);
    }

    @Transactional
    public Prestador adicionarTelefone(Integer prestadorId, TelefoneDTO telefoneDTO) {
        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Telefone novoTelefone = new Telefone();
        novoTelefone.setNumero(telefoneDTO.getNumero());
        novoTelefone.setWhatsapp(telefoneDTO.isWhatsapp());
        novoTelefone.setPrestador(prestador); 

        prestador.getTelefones().add(novoTelefone);

        return prestadorRepository.save(prestador);
    }

    @Transactional
    public Telefone atualizarTelefone(Integer prestadorId, Integer telefoneId, TelefoneDTO telefoneDTO) {
        // Garante que o prestador existe
        prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado"));

        // Validação de segurança: verifica se o telefone realmente pertence ao prestador informado
        if (!telefone.getPrestador().getId().equals(prestadorId)) {
            throw new ResourceNotFoundException("Este telefone não pertence ao prestador informado.");
        }

        telefone.setNumero(telefoneDTO.getNumero());
        telefone.setWhatsapp(telefoneDTO.isWhatsapp());

        return telefoneRepository.save(telefone);
    }

    @Transactional
    public void deletarTelefone(Integer prestadorId, Integer telefoneId) {
        // Garante que o prestador existe
        prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado"));

        // Validação de segurança
        if (!telefone.getPrestador().getId().equals(prestadorId)) {
            throw new ResourceNotFoundException("Este telefone não pertence ao prestador informado.");
        }

        telefoneRepository.deleteById(telefoneId);
    }

    public Prestador adicionarCategoria(Integer prestadorId, Integer categoriaId) {
        // Busca o prestador e a categoria no banco de dados
        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        
        // Adiciona a categoria ao conjunto de categorias do prestador
        prestador.getCategorias().add(categoria);

        // Salva o prestador, o JPA vai cuidar de atualizar a tabela de junção
        return prestadorRepository.save(prestador);
    }

    @Transactional
    public Prestador salvar(PrestadorDTO prestadorDTO) {
        
        // 1. Converte DTO para Entidade Prestador
        Prestador prestador = new Prestador();
        prestador.setNomeCompleto(prestadorDTO.getNomeCompleto());
        prestador.setNomeFantasia(prestadorDTO.getNomeFantasia());
        prestador.setEmail(prestadorDTO.getEmail());
        prestador.setSenhaHash(passwordEncoder.encode(prestadorDTO.getSenha())); // <- (Garantindo que a criptografia esteja aqui)
        prestador.setBio(prestadorDTO.getBio());
        prestador.setFotoPerfilUrl(prestadorDTO.getFotoPerfilUrl());
        prestador.setCpf(prestadorDTO.getCpf());

        // 2. Lógica para adicionar o Telefone (que estava faltando)
        if (prestadorDTO.getTelefoneNumero() != null && !prestadorDTO.getTelefoneNumero().isEmpty()) {
            Telefone telefone = new Telefone();
            telefone.setNumero(prestadorDTO.getTelefoneNumero());
            telefone.setWhatsapp(prestadorDTO.getIsWhatsapp() != null && prestadorDTO.getIsWhatsapp());
            
            // Define o link bidirecional (JPA gerencia o salvamento via cascade)
            telefone.setPrestador(prestador);
            prestador.getTelefones().add(telefone); 
        }

        // 3. Lógica para associar as Categorias (que já tínhamos)
        if (prestadorDTO.getCategoriaIds() != null && !prestadorDTO.getCategoriaIds().isEmpty()) {
            for (Integer categoriaId : prestadorDTO.getCategoriaIds()) {
                Categoria categoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o id: " + categoriaId));
                
                // (Assumindo que o 'addCategoria' bidirecional está no Prestador.java)
                prestador.addCategoria(categoria); 
            }
        }
        
        // 4. Salva o Prestador (e suas coleções em cascade)
        Prestador prestadorSalvo = prestadorRepository.saveAndFlush(prestador);

        // 5. A CORREÇÃO FINAL: Re-busca do banco para retornar o JSON completo
        // Isso força o Hibernate a carregar as coleções 'telefones' e 'categorias'
        // que acabaram de ser salvas, para que apareçam na resposta.
        return prestadorRepository.findById(prestadorSalvo.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao re-buscar prestador após salvar: " + prestadorSalvo.getId()));
    }

    // --- Lógica do Relacionamento ---
    
}