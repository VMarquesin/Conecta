import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';
import { PerfilService } from '../../services/perfil';
import { Observable } from 'rxjs';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

// 1. Importe os serviços de ATUALIZAÇÃO e os DTOs
import { PrestadorService, PrestadorDTO } from '../../services/prestador.service';
import { ClienteService, ClienteDTO } from '../../services/cliente'; 

@Component({
  selector: 'app-meu-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './meu-perfil.html',
  styleUrl: './meu-perfil.css',
})
export class MeuPerfilComponent implements OnInit {
  
  perfil$: Observable<any>;
  perfilForm: FormGroup;
  isPrestador: boolean = false;
  
  private meuId: number = 0; // Variável para guardar o ID do usuário
  mensagemSucesso: string = ''; // Para feedback

  constructor(
    public authService: AuthService,
    private perfilService: PerfilService,
    private fb: FormBuilder,
    // 2. Injete os serviços de atualização
    private prestadorService: PrestadorService,
    private clienteService: ClienteService
  ) {
    
    this.perfilForm = this.fb.group({
      nomeCompleto: [''],
      email: [''], // O backend ignora, mas o form precisa dele
      cpf: [''], // O backend ignora, mas o form precisa dele
      nomeFantasia: [''],
      bio: [''],
    });

    this.perfil$ = this.perfilService.getMeuPerfil();
  }

  ngOnInit(): void {
    this.perfil$.subscribe((dadosDoPerfil) => {
      
      if (this.authService.hasRole('ROLE_PRESTADOR')) {
        this.isPrestador = true;
      }
      
      // 3. Guardamos o ID do usuário logado
      this.meuId = dadosDoPerfil.id;

      // 4. Preenchemos o formulário
      this.perfilForm.patchValue({
        nomeCompleto: dadosDoPerfil.nomeCompleto,
        email: dadosDoPerfil.email,
        cpf: dadosDoPerfil.cpf,
        nomeFantasia: dadosDoPerfil.nomeFantasia || '',
        bio: dadosDoPerfil.bio || '',
      });
    });
  }

  // 5. ESTE É O MÉTODO ATUALIZADO
  onSalvarAlteracoes() {
    if (this.perfilForm.invalid) {
      return;
    }
    
    // Pega os dados do formulário
    const dadosFormulario = this.perfilForm.value;
    this.mensagemSucesso = ''; // Limpa a mensagem

    // 6. Decide qual serviço chamar
    if (this.isPrestador) {
      // Se for PRESTADOR, chama o prestadorService
      this.prestadorService.atualizar(this.meuId, dadosFormulario).subscribe({
        next: () => {
          this.mensagemSucesso = "Perfil de Prestador atualizado com sucesso!";
        },
        error: (err) => {
          console.error("Erro ao atualizar prestador", err);
          this.mensagemSucesso = "Erro ao atualizar perfil. Tente novamente.";
        }
      });
    } else {
      // Se for CLIENTE, chama o clienteService
      this.clienteService.atualizar(this.meuId, dadosFormulario).subscribe({
        next: () => {
          this.mensagemSucesso = "Perfil de Cliente atualizado com sucesso!";
        },
        error: (err) => {
          console.error("Erro ao atualizar cliente", err);
          this.mensagemSucesso = "Erro ao atualizar perfil. Tente novamente.";
        }
      });
    }
  }
}