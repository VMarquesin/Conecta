import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

// Este é o novo formato "funcional" de interceptors do Angular
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  
  // 1. Injeta o nosso serviço de autenticação
  const authService = inject(AuthService);
  
  // 2. Pega o token salvo
  const token = authService.getToken();

  // 3. Verifica se o token existe e se a requisição é para a nossa API
  // (Não queremos enviar o token para outros sites, ex: se carregarmos uma imagem de outro lugar)
  if (token && req.url.startsWith('http://localhost:8080/api/')) {
    
    // 4. Clona a requisição original e adiciona o cabeçalho de Autorização
    const clonedReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });

    // 5. Envia a requisição clonada (com o token)
    return next(clonedReq);
  }

  // 6. Se não houver token, ou a requisição for para outro site,
  // envia a requisição original sem modificação.
  return next(req);
};