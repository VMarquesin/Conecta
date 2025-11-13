import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// Interface para o DTO que o backend espera
export interface PublicacaoDTO {
  titulo: string;
  descricao: string;
  fotoUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class PublicacaoService {

  // A URL base da API
  private apiUrl = 'http://localhost:8080/api'; 

  constructor(private http: HttpClient) { }

  /**
   * Rota de Salvar (POST /api/publicacoes)
   * O backend usa o token para identificar o prestador.
   */
  salvar(publicacao: PublicacaoDTO): Observable<any> {
    // CORRIGIDO: Removido o prestadorId da chamada
    return this.http.post(`${this.apiUrl}/publicacoes`, publicacao);
  }
  
  /**
   * Rota de Atualizar (PUT /api/publicacoes/{id})
   */
  atualizar(publicacaoId: number, publicacao: PublicacaoDTO): Observable<any> {
    return this.http.put(`${this.apiUrl}/publicacoes/${publicacaoId}`, publicacao);
  }

  /**
   * Rota de Deletar (DELETE /api/publicacoes/{id})
   */
  deletar(publicacaoId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/publicacoes/${publicacaoId}`);
  }
}