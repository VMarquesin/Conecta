package br.com.conecta.controller;

import br.com.conecta.dto.LoginRequestDTO;
import br.com.conecta.dto.LoginResponseDTO;
import br.com.conecta.security.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Definimos um novo caminho base para todos os endpoints de autenticação
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // O "Gerente" que configuramos no SecurityConfig

    @Autowired
    private JwtTokenService jwtTokenService; // O serviço que criamos para gerar o token

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        // 1. Cria um "pacote" com o email e a senha que o usuário enviou
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getSenha());

        // 2. O AuthenticationManager tenta autenticar.
        // Ele vai automaticamente chamar nosso CustomUserDetailsService para buscar o usuário
        // e nosso PasswordEncoder para comparar as senhas.
        // Se a senha estiver errada, ele lança uma exceção (que o nosso GlobalExceptionHandler pegará)
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        // 3. Se chegou até aqui, o usuário está AUTENTICADO com sucesso.
        // Pegamos os detalhes do usuário que foi autenticado.
        UserDetails userDetails = (UserDetails) authenticationResponse.getPrincipal();

        // 4. Usamos nosso JwtTokenService para gerar um token para este usuário.
        String token = jwtTokenService.generateToken(userDetails);

        // 5. Retornamos o token em um DTO de resposta.
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}