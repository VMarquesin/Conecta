import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
// 1. Importe a biblioteca que acabamos de instalar
import { jwtDecode } from 'jwt-decode'; 

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'authToken';

  // 2. NOVOS "ANUNCIANTES" (BehaviorSubjects)
  // Anunciante para o status de login (como antes)
  private loggedIn = new BehaviorSubject<boolean>(false);
  // Anunciante para os "papéis" (Roles) do usuário
  private userRoles = new BehaviorSubject<string[]>([]);

  // 3. OBSERVABLES PÚBLICOS
  // Componentes vão "ouvir" estes para saber o status
  isLoggedIn$: Observable<boolean> = this.loggedIn.asObservable();
  userRoles$: Observable<string[]> = this.userRoles.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // 4. VERIFICAÇÃO INICIAL
    // Quando o serviço carrega, verifique se já existe um token
    // (isso mantém o usuário logado se ele der F5 na página)
    this.checkTokenOnLoad();
  }

  private checkTokenOnLoad(): void {
    const token = this.getToken();
    if (token) {
      this.decodeToken(token); // Decodifica o token e atualiza os "anunciantes"
    }
  }

  /**
   * Faz o login, salva o token e atualiza os anunciantes.
   */
  login(credentials: { email: string, senha: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          this.decodeToken(response.token); // Decodifica o novo token
        }
      })
    );
  }

  /**
   * Faz o logout, limpa o token e reseta os anunciantes.
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.loggedIn.next(false); // Anuncia o logout
    this.userRoles.next([]);   // Limpa os papéis
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return this.loggedIn.value; // Retorna o valor atual do anunciante
  }

  // 5. NOVO MÉTODO: Decodificador de Token
  private decodeToken(token: string): void {
    try {
      // Usa a biblioteca jwt-decode para ler o conteúdo do token
      const decodedToken: any = jwtDecode(token);
      
      // Busca o "carimbo" de papéis (roles) que colocamos no backend
      const roles = decodedToken.roles ? decodedToken.roles.split(',') : [];
      
      // Atualiza os anunciantes
      this.userRoles.next(roles);
      this.loggedIn.next(true);

    } catch (Error) {
      // Se o token for inválido ou expirar, faz o logout
      this.logout();
    }
  }

  // 6. NOVO MÉTODO: Verificador de Papel
  // Um método fácil para os componentes perguntarem "Este usuário é um PRESTADOR?"
  hasRole(role: string): boolean {
    return this.userRoles.value.includes(role);
  }
}