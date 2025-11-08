import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// Interface para o DTO que o backend espera
export interface PublicacaoDTO {
  titulo: string;
  descricao: string;
  fotoUrl?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PublicacaoService {

  private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) { }

  /**
   * Salva uma nova publicação para um prestador específico.
   */
  salvar(prestadorId: number, publicacao: PublicacaoDTO): Observable<any> {
    return this.http.post(`${this.apiUrl}/${prestadorId}/publicacoes`, publicacao);
  }
}