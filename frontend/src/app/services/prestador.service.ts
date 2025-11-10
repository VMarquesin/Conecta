import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Esta é a interface para os dados que vêm da listagem e do detalhe
export interface Prestador {
  id: number;
  nomeCompleto: string;
  nomeFantasia: string;
  email: string;
  telefoneNumero?: string;
  bio: string;
}

// Interface para o DTO de cadastro
export interface PrestadorDTO {
  nomeCompleto: string;
  email: string;
  senha: string;
  cpf: string;
  telefoneNumero?: string;
  isWhatsapp?: boolean;
  nomeFantasia?: string;
  bio?: string;
  categoriaIds?: number[];
}

// Interface para Publicações
export interface Publicacao {
  id: number;
  titulo: string;
  descricao: string;
  fotoUrl: string;
  dataPublicacao: string;
}

// Interface para Avaliações
export interface AvaliacaoResponse {
  id: number;
  nota: number;
  comentario: string;
  dataAvaliacao: string;
  nomeCliente: string;
}

@Injectable({
  providedIn: 'root',
})
export class PrestadorService {
  private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) {}

  getPrestadores(): Observable<Prestador[]> {
    return this.http.get<Prestador[]>(this.apiUrl);
  }

  salvar(prestador: PrestadorDTO): Observable<any> {
    return this.http.post(this.apiUrl, prestador);
  }

  getPrestadorById(id: number): Observable<Prestador> {
    return this.http.get<Prestador>(`${this.apiUrl}/${id}`);
  }

  getPublicacoesPorPrestador(id: number): Observable<Publicacao[]> {
    return this.http.get<Publicacao[]>(`${this.apiUrl}/${id}/publicacoes`);
  }

  getAvaliacoesPorPrestador(id: number): Observable<AvaliacaoResponse[]> {
    // CORREÇÃO: A URL agora aponta para a nova rota de avaliações do backend
    return this.http.get<AvaliacaoResponse[]>(
      `http://localhost:8080/api/avaliacoes/prestador/${id}`
    );
  }
  atualizar(id: number, prestadorData: PrestadorDTO): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, prestadorData);
  }
}
