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
  // CORREÇÃO 1: A URL base deve ser a raiz da API
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  salvar(prestadorId: number, publicacao: PublicacaoDTO): Observable<any> {
    // CORREÇÃO 2: A rota de salvar é uma sub-rota de 'prestadores'
    return this.http.post(`${this.apiUrl}/prestadores/${prestadorId}/publicacoes`, publicacao);
  }

  deletar(prestadorId: number, publicacaoId: number): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/prestadores/${prestadorId}/publicacoes/${publicacaoId}`
    );
  }
  atualizar(prestadorId: number, publicacaoId: number, publicacao: PublicacaoDTO): Observable<any> {
    return this.http.put(
      `${this.apiUrl}/prestadores/${prestadorId}/publicacoes/${publicacaoId}`,
      publicacao
    );
  }
}
