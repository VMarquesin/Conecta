import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// Interface para os dados que vamos enviar (corresponde ao nosso ClienteDTO)
export interface ClienteDTO {
  nomeCompleto: string;
  email: string;
  senha: string;
  cpf: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private apiUrl = 'http://localhost:8080/api/clientes';

  constructor(private http: HttpClient) { }

  // Método para enviar os dados do novo cliente para a API
  salvar(cliente: ClienteDTO): Observable<any> {
    return this.http.post(this.apiUrl, cliente);
  }
}