package br.com.conecta.controller;

import br.com.conecta.repository.ClienteRepository;
import br.com.conecta.repository.PrestadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestadorRepository prestadorRepository;

    /**
     * Endpoint "Quem sou eu".
     * Busca os dados do usuário (Cliente ou Prestador) com base no token JWT.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMeuPerfil() {
        // 1. Pega a autenticação do token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // 2. Verifica se é um Cliente
        var clienteOpt = clienteRepository.findByEmail(email);
        if (clienteOpt.isPresent()) {
            return ResponseEntity.ok(clienteOpt.get());
        }

        // 3. Se não, verifica se é um Prestador
        var prestadorOpt = prestadorRepository.findByEmail(email);
        if (prestadorOpt.isPresent()) {
            return ResponseEntity.ok(prestadorOpt.get());
        }

        // 4. Se não encontrar, retorna erro
        throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
    }
}