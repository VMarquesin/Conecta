import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Definindo uma interface para os dados do Prestador
export interface Prestador {
  id: number;
  nomeCompleto: string;
  nomeFantasia: string;
  email: string;
  bio: string;
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
}