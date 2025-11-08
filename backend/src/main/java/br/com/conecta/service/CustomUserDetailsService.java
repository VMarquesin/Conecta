package br.com.conecta.service;

import br.com.conecta.entity.Cliente;
import br.com.conecta.entity.Prestador;
import br.com.conecta.repository.ClienteRepository;
import br.com.conecta.repository.PrestadorRepository;
import org.springframework.beans.factory.annotation.Autowired;

// 1. Importe estas classes
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List; // Importe
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
            
            // 2. CRIA A "ROLE_CLIENTE"
            // Define o "papel" deste usuário
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));

            // 3. Retorna o UserDetails com o papel
            return new User(c.getEmail(), c.getSenhaHash(), authorities);
        }

        // 4. Se não encontrar, tenta encontrar na tabela de Prestadores
        Optional<Prestador> prestador = prestadorRepository.findByEmail(email);
        if (prestador.isPresent()) {
            Prestador p = prestador.get();
            
            // 5. CRIA A "ROLE_PRESTADOR"
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_PRESTADOR"));

            // 6. Retorna o UserDetails com o papel
            return new User(p.getEmail(), p.getSenhaHash(), authorities);
        }

        // 7. Se não encontrar em nenhuma tabela, lança a exceção
        throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
    }
}