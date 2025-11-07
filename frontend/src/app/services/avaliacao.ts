import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// Esta é a interface do DTO que o backend espera (nota, comentario, clienteId)
export interface AvaliacaoDTO {
  nota: number;
  comentario: string;
  clienteId: number;
}

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {

  // A URL base da nossa API de avaliações
  private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) { }

  /**
   * Salva uma nova avaliação para um prestador específico.
   */
  salvar(prestadorId: number, avaliacao: AvaliacaoDTO): Observable<any> {
    // Constrói a URL completa, ex: /api/prestadores/1/avaliacoes
    return this.http.post(`${this.apiUrl}/${prestadorId}/avaliacoes`, avaliacao);
  }
}