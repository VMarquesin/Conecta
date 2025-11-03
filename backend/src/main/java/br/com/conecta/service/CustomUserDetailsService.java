package br.com.conecta.service;

import br.com.conecta.entity.Cliente;
import br.com.conecta.entity.Prestador;
import br.com.conecta.repository.ClienteRepository;
import br.com.conecta.repository.PrestadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Tenta encontrar o usuário na tabela de Clientes
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        if (cliente.isPresent()) {
            Cliente c = cliente.get();
            // Retorna um objeto 'User' do Spring Security
            return new User(c.getEmail(), c.getSenhaHash(), new ArrayList<>());
        }

        // 2. Se não encontrar, tenta encontrar na tabela de Prestadores
        Optional<Prestador> prestador = prestadorRepository.findByEmail(email);
        if (prestador.isPresent()) {
            Prestador p = prestador.get();
            // Retorna um objeto 'User' do Spring Security
            return new User(p.getEmail(), p.getSenhaHash(), new ArrayList<>());
        }

        // 3. Se não encontrar em nenhuma tabela, lança a exceção
        throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
    }
}