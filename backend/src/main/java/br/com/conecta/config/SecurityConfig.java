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

    // Este @Bean cria um objeto "PasswordEncoder" que fica disponível para toda a aplicação.
    // Estamos dizendo ao Spring: "Sempre que alguém pedir um PasswordEncoder,
    // entregue esta instância do BCryptPasswordEncoder".
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Este @Bean define a cadeia de filtros de segurança.
    // Por enquanto, estamos configurando para PERMITIR TODAS as requisições,
    // para não travar nossa API enquanto desenvolvemos.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita a proteção CSRF, que não é necessária para APIs stateless.
            .csrf(csrf -> csrf.disable())
            // Define as regras de autorização.
            .authorizeHttpRequests(auth -> auth
                // Permite que qualquer requisição seja acessada sem autenticação.
                .anyRequest().permitAll()
            );
        return http.build();
    }
}