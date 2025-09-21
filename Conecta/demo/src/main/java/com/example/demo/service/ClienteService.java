package com.example.demo.service;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.dto.ClienteResponseDTO;
import com.example.demo.exception.ResourceNotFoundException; 

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
   
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
        cliente.setSenhaHash(clienteDTO.getSenha()); // Simplificado: sem criptografia por enquanto
        cliente.setCpf(clienteDTO.getCpf());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Integer id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));

        clienteExistente.setNomeCompleto(clienteDTO.getNomeCompleto());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setCpf(clienteDTO.getCpf());
        if (clienteDTO.getSenha() != null && !clienteDTO.getSenha().isEmpty()) {
            clienteExistente.setSenhaHash(clienteDTO.getSenha());
        }
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