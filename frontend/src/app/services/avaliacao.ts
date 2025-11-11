import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// Esta é a interface do DTO que o backend espera (nota, comentario, clienteId)
export interface AvaliacaoDTO {
  nota: number;
  comentario: string;
}

@Injectable({
  providedIn: 'root',
})
export class AvaliacaoService {
  // A URL base da nossa API de avaliações
  private apiUrl = 'http://localhost:8080/api/avaliacoes';
  // private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) {}

  /**
   * Salva uma nova avaliação para um prestador específico.
   */
  salvarParaPrestador(prestadorId: number, avaliacao: AvaliacaoDTO): Observable<any> {
    return this.http.post(
      `http://localhost:8080/api/avaliacoes/prestador/${prestadorId}`,
      avaliacao
    );
  }

  // MÉTODO NOVO
  salvarParaPublicacao(publicacaoId: number, avaliacao: AvaliacaoDTO): Observable<any> {
    return this.http.post(
      `http://localhost:8080/api/avaliacoes/publicacao/${publicacaoId}`,
      avaliacao
    );
  }

  deletar(avaliacaoId: number): Observable<any> {
    // Chama a nova rota protegida: DELETE /api/avaliacoes/{id}
    return this.http.delete(`${this.apiUrl}/${avaliacaoId}`);
  }

  atualizar(avaliacaoId: number, avaliacao: AvaliacaoDTO): Observable<any> {
    return this.http.put(`${this.apiUrl}/${avaliacaoId}`, avaliacao);
  }
}
