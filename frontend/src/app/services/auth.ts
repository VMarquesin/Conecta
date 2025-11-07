import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
// 1. Importe BehaviorSubject e Observable
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'authToken';

  // 2. O "Anunciante" do Status de Login
  // BehaviorSubject é um tipo especial de Observable que armazena o último valor
  // e o "anuncia" para qualquer novo assinante.
  // Começa como 'false' (usuário não está logado).
  private loggedIn = new BehaviorSubject<boolean>(this.isLoggedIn());

  // 3. Transformamos o "anunciante" em um Observable público
  // Componentes vão "ouvir" este '$' para saber se o usuário está logado.
  isLoggedIn$: Observable<boolean> = this.loggedIn.asObservable();

  constructor(private http: HttpClient, private router: Router) { }

  login(credentials: { email: string, senha: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          // 4. ANUNCIA A MUDANÇA: O usuário está logado!
          this.loggedIn.next(true);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    // 5. ANUNCIA A MUDANÇA: O usuário fez logout!
    this.loggedIn.next(false);
    // 6. Envia o usuário de volta para a home
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // A lógica desta função mudou um pouco
  isLoggedIn(): boolean {
    const token = this.getToken();
    return !!token; // Continua sendo true se houver um token
  }
}