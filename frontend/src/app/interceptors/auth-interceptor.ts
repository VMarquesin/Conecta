import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  
  // serviço de autenticação
  const authService = inject(AuthService);
  
  // Pega o token
  const token = authService.getToken();

  if (token && req.url.startsWith('http://localhost:8080/api/')) {
    const clonedReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(clonedReq);
  }
  return next(req);
};