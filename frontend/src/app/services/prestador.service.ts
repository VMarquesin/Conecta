import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interface para os DADOS DE RESPOSTA (o que vem da listagem)
export interface Prestador {
  id: number;
  nomeCompleto: string;
  nomeFantasia: string;
  email: string;
  bio: string;
  // adicione outros campos que a API retorna na listagem
}

// Interface para os DADOS DE ENVIO (o que vai no cadastro)
export interface PrestadorDTO {
  nomeCompleto: string;
  email: string;
  senha: string;
  cpf: string;
  nomeFantasia?: string;
  bio?: string;
  categoriaIds?: number[];
}

@Injectable({
  providedIn: 'root' // <-- CORREÇÃO 1: Completar a anotação
})
export class PrestadorService {

  private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) { }

  // Este método agora usa a interface 'Prestador' que definimos acima
  getPrestadores(): Observable<Prestador[]> {
    return this.http.get<Prestador[]>(this.apiUrl);
  }

  // Este método usa a interface 'PrestadorDTO'
  salvar(prestador: PrestadorDTO): Observable<any> {
    return this.http.post(this.apiUrl, prestador);
  }
}