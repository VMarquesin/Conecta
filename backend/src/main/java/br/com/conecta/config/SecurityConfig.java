package br.com.conecta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            // --- ADICIONE ESTAS 3 LINHAS ABAIXO ---
            // Isso permite que o H2 Console seja renderizado em um frame
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            
            .authorizeHttpRequests(auth -> auth
                // Permite que o H2 Console seja acessado
                .requestMatchers("/h2-console/**").permitAll() 
                // Mantém nossas regras existentes
                .anyRequest().permitAll()
            );
        return http.build();
    }
}