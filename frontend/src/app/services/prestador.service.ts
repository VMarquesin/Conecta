import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Prestador {
  id: number;
  nomeCompleto: string;
  nomeFantasia: string;
  email: string;
  bio: string;
}


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
  providedIn: 'root' 
})
export class PrestadorService {

  private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) { }

  getPrestadores(): Observable<Prestador[]> {
    return this.http.get<Prestador[]>(this.apiUrl);
  }

  salvar(prestador: PrestadorDTO): Observable<any> {
    return this.http.post(this.apiUrl, prestador);
  }
}