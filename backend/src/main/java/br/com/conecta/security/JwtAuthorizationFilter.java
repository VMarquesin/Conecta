package br.com.conecta.security;

import br.com.conecta.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Pega o token do cabeçalho da requisição
            String jwt = getJwtFromRequest(request);

            // 2. Valida o token
            if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
                
                // 3. Se for válido, extrai o email
                String email = jwtTokenService.getUsernameFromToken(jwt);
                
                // 4. Carrega os dados do usuário do banco
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                
                // 5. Cria um objeto de "Autenticação" do Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Define o usuário como "logado" para esta requisição
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Se algo der errado (token inválido, etc.), apenas registramos o erro
            logger.error("Não foi possível definir a autenticação do usuário", ex);
        }

        // 7. Passa a requisição para o próximo filtro na cadeia
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar para extrair o Token do cabeçalho "Authorization".
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        // O cabeçalho deve ser assim: "Bearer [token]"
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Retorna apenas a string do token
        }
        return null;
    }
}