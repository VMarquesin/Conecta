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
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // SecurityConfig

    @Autowired
    private JwtTokenService jwtTokenService; // token

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getSenha());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        UserDetails userDetails = (UserDetails) authenticationResponse.getPrincipal();
        String token = jwtTokenService.generateToken(userDetails);
        
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}