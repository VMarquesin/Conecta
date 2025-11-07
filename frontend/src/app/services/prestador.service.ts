import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// 1. A INTERFACE 'Prestador' QUE ESTAVA FALTANDO
// Esta é a interface para os dados que vêm da listagem e do detalhe
export interface Prestador {
  id: number;
  nomeCompleto: string;
  nomeFantasia: string;
  email: string;
  bio: string;
  // adicione outros campos que a API retorna
}

// Interface para o DTO de cadastro
export interface PrestadorDTO {
  nomeCompleto: string;
  email: string;
  senha: string;
  cpf: string;
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
  providedIn: 'root'
})
export class PrestadorService {

  private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) { }

  // 2. O MÉTODO 'getPrestadores' QUE ESTAVA FALTANDO
  getPrestadores(): Observable<Prestador[]> {
    return this.http.get<Prestador[]>(this.apiUrl);
  }

  // 3. O MÉTODO 'salvar' QUE ESTAVA FALTANDO
  salvar(prestador: PrestadorDTO): Observable<any> {
    return this.http.post(this.apiUrl, prestador);
  }

  // --- NOSSOS NOVOS MÉTODOS ---

  getPrestadorById(id: number): Observable<Prestador> {
    return this.http.get<Prestador>(`${this.apiUrl}/${id}`);
  }

  getPublicacoesPorPrestador(id: number): Observable<Publicacao[]> {
    return this.http.get<Publicacao[]>(`${this.apiUrl}/${id}/publicacoes`);
  }

  getAvaliacoesPorPrestador(id: number): Observable<AvaliacaoResponse[]> {
    return this.http.get<AvaliacaoResponse[]>(`${this.apiUrl}/${id}/avaliacoes`);
  }
}