import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {
  private apiUrl = 'http://localhost:8080/api/perfil';

  constructor(private http: HttpClient) { }

  /**
   * Busca os dados do perfil (Cliente ou Prestador) do usuário logado.
   * O token JWT será anexado automaticamente pelo nosso interceptor.
   */
  getMeuPerfil(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me`);
  }
}