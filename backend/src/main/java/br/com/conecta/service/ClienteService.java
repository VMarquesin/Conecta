package br.com.conecta.service;

import br.com.conecta.dto.ClienteDTO;
import br.com.conecta.entity.Cliente;
import br.com.conecta.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.conecta.dto.ClienteResponseDTO;
import br.com.conecta.exception.ResourceNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.conecta.dto.PerfilUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
   
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<ClienteResponseDTO> buscarPorId(Integer id) {
        return clienteRepository.findById(id).map(ClienteResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Cliente salvar(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto(clienteDTO.getNomeCompleto());
        cliente.setEmail(clienteDTO.getEmail());
        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(clienteDTO.getSenha());
        cliente.setSenhaHash(senhaCriptografada);
        
        cliente.setCpf(clienteDTO.getCpf());
        return clienteRepository.save(cliente);
    }

    // Dentro de ClienteService.java

    @Transactional
    public Cliente atualizarPerfil(Integer id, PerfilUpdateDTO perfilDTO) {
        // 1. Pega o email do usuário logado (do token)
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Busca o cliente no banco
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));

        // 3. VERIFICAÇÃO DE DONO (Segurança)
        if (!clienteExistente.getEmail().equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Você não tem permissão para editar o perfil de outro usuário.");
        }

        // 4. Atualiza apenas os campos permitidos (Nome)
        // (Clientes no nosso sistema por enquanto só editam o nome no perfil básico)
        clienteExistente.setNomeCompleto(perfilDTO.getNomeCompleto());
        
        // Se quiser permitir editar outros campos no futuro (como telefone), adicione aqui.
        // Não atualizamos senha, email ou CPF aqui.

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}