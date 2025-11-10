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

    @Transactional
    public Cliente atualizar(Integer id, ClienteDTO clienteDTO) {
        // 1. Pega o email do usuário logado (do token JWT)
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));

        // 2. VERIFICAÇÃO DE DONO
        if (!clienteExistente.getEmail().equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Você não tem permissão para editar o perfil de outro usuário.");
        }

        // 3. Atualiza apenas os campos permitidos
        clienteExistente.setNomeCompleto(clienteDTO.getNomeCompleto());
        // NOTA: Email, CPF e Senha não são atualizados aqui por segurança.

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