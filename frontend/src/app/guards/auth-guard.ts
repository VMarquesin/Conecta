import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

// ANTES (Errado): import { AuthService } from '../services/auth.service';
// DEPOIS (Correto - sem o '.service'):
import { AuthService } from '../services/auth';

// Este é o novo formato "funcional" de Guardas
export const authGuard: CanActivateFn = (route, state) => {
  
  // 1. Injeta os serviços que precisamos
  const authService = inject(AuthService); // Agora isso vai funcionar
  const router = inject(Router);

  // 2. Verifica if o usuário está logado
  if (authService.isLoggedIn()) {
    // Se sim, permite o acesso
    return true;
  } else {
    // 3. Se não, redireciona o usuário para a página de login
    router.navigate(['/login']);
    // E bloqueia o acesso
    return false;
  }
};