import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'authToken'; // Chave para salvar o token no localStorage

  constructor(private http: HttpClient) { }

  /**
   * Envia as credenciais para a API e salva o token JWT no sucesso.
   */
  login(credentials: { email: string, senha: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        // 'tap' nos permite "espiar" a resposta sem modificá-la
        if (response && response.token) {
          // Salva o token no localStorage do navegador
          localStorage.setItem(this.TOKEN_KEY, response.token);
        }
      })
    );
  }

  /**
   * Remove o token do localStorage (logout).
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  /**
   * Pega o token salvo no localStorage.
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Verifica se o usuário está logado (se existe um token).
   */
  isLoggedIn(): boolean {
    return !!this.getToken(); // O '!!' transforma a string (ou null) em um booleano
  }
}