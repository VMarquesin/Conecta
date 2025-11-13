import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';
import { PerfilService } from '../../services/perfil';
import { Observable, BehaviorSubject, switchMap } from 'rxjs';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

// 1. Importe os serviços e interfaces de Publicação
import { ClienteService, ClienteDTO } from '../../services/cliente'; 
import { PrestadorService, PrestadorDTO, Publicacao } from '../../services/prestador.service';
import { PublicacaoService } from '../../services/publicacao';

@Component({
  selector: 'app-meu-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './meu-perfil.html',
  styleUrl: './meu-perfil.css',
})
export class MeuPerfilComponent implements OnInit {
  
  // Variáveis do Perfil
  perfil$: Observable<any>;
  perfilForm: FormGroup;
  isPrestador: boolean = false;
  private meuId: number = 0;
  mensagemSucesso: string = '';

  // 2. VARIÁVEIS DE PUBLICAÇÃO (MOVIDAS PARA CÁ)
  publicacoes$!: Observable<Publicacao[]>;
  private refreshPublicacoes = new BehaviorSubject<void>(undefined);
  publicacaoForm: FormGroup;
  mensagemSucessoPublicacao: string = '';
  editandoPublicacaoId: number | null = null;
  
  constructor(
    public authService: AuthService,
    private perfilService: PerfilService,
    private fb: FormBuilder,
    private prestadorService: PrestadorService,
    private clienteService: ClienteService,
    // 3. SERVIÇO DE PUBLICAÇÃO INJETADO AQUI
    private publicacaoService: PublicacaoService 
  ) {
    
    // Formulário de Edição de Perfil (você já tinha)
    this.perfilForm = this.fb.group({
      nomeCompleto: [''],
      email: [''],
      cpf: [''],
      nomeFantasia: [''],
      bio: [''],
    });

    // 4. INICIALIZAÇÃO DO FORMULÁRIO DE PUBLICAÇÃO (MOVIDO PARA CÁ)
    this.publicacaoForm = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      fotoUrl: [''],
    });

    this.perfil$ = this.perfilService.getMeuPerfil();
  }

  ngOnInit(): void {
    this.perfil$.subscribe((dadosDoPerfil) => {
      
      this.meuId = dadosDoPerfil.id; // Guarda o ID do usuário

      if (this.authService.hasRole('ROLE_PRESTADOR')) {
        this.isPrestador = true;

        // 5. LÓGICA DE BUSCAR PUBLICAÇÕES (NO LUGAR CERTO)
        // Busca as publicações DESTE prestador logado
        this.publicacoes$ = this.refreshPublicacoes.pipe(
          switchMap(() => this.prestadorService.getPublicacoesPorPrestador(this.meuId))
        );
      }
      
      // Preenche o formulário de perfil
      this.perfilForm.patchValue({
        nomeCompleto: dadosDoPerfil.nomeCompleto,
        email: dadosDoPerfil.email,
        cpf: dadosDoPerfil.cpf,
        nomeFantasia: dadosDoPerfil.nomeFantasia || '',
        bio: dadosDoPerfil.bio || '',
      });
    });
  }

  // Método de salvar o Perfil (você já tinha)
  onSalvarAlteracoes() {
    if (this.perfilForm.invalid) { return; }
    
    const dadosFormulario = this.perfilForm.value;
    this.mensagemSucesso = ''; 

    if (this.isPrestador) {
      this.prestadorService.atualizar(this.meuId, dadosFormulario).subscribe({
        next: () => { this.mensagemSucesso = "Perfil de Prestador atualizado com sucesso!"; },
        error: (err) => { 
          console.error("Erro ao atualizar prestador", err);
          this.mensagemSucesso = "Erro ao atualizar perfil. Tente novamente.";
         }
      });
    } else {
      this.clienteService.atualizar(this.meuId, dadosFormulario).subscribe({
        next: () => { this.mensagemSucesso = "Perfil de Cliente atualizado com sucesso!"; },
        error: (err) => { /* ... (código de erro) ... */ }
      });
    }
  }

  // ---------------------------------------------------
  // 6. MÉTODOS DE GERENCIAMENTO DE PUBLICAÇÃO (MOVIDOS PARA CÁ)
  // ---------------------------------------------------

  onCarregarParaEditar(publicacao: Publicacao) {
    this.editandoPublicacaoId = publicacao.id;
    this.publicacaoForm.patchValue({
      titulo: publicacao.titulo,
      descricao: publicacao.descricao,
      fotoUrl: publicacao.fotoUrl,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

 // Dentro da classe MeuPerfilComponent

  onPostarPublicacao() {
    if (this.publicacaoForm.invalid) { return; }

    if (this.editandoPublicacaoId !== null) {
      // A lógica de ATUALIZAR (PUT) já está correta
      this.publicacaoService
        .atualizar(this.editandoPublicacaoId, this.publicacaoForm.value)
        .subscribe({
          next: () => {
            this.mensagemSucessoPublicacao = 'Publicação ATUALIZADA com sucesso!';
            this.publicacaoForm.reset();
            this.editandoPublicacaoId = null;
            this.refreshPublicacoes.next();
          },
          error: (err) => { 
            console.error('Erro ao atualizar publicação:', err); 
            this.mensagemSucessoPublicacao = 'Erro ao atualizar. (Verifique o console)';
          },
        });
      return; 
    }

    // --- CORREÇÃO NA LÓGICA DE CRIAR (POST) ---
    // A chamada de 'salvar' não precisa mais do 'meuId'
    this.publicacaoService.salvar(this.publicacaoForm.value).subscribe({
      next: () => {
        this.mensagemSucessoPublicacao = 'Publicação CRIADA com sucesso!';
        this.publicacaoForm.reset();
        this.refreshPublicacoes.next();
      },
      error: (err) => { 
        console.error('Erro ao criar publicação:', err); 
        this.mensagemSucessoPublicacao = 'Erro ao criar. (Verifique o console)';
      },
    });
  }

  cancelarEdicao() {
    this.editandoPublicacaoId = null;
    this.publicacaoForm.reset();
  }

  onDeletarPublicacao(publicacaoId: number) {
    if (confirm('Tem certeza que deseja deletar esta publicação?')) {
      this.publicacaoService.deletar(publicacaoId).subscribe({
        next: () => {
          alert('Publicação deletada com sucesso!');
          this.refreshPublicacoes.next();
        },
        error: (err) => { console.error('Erro ao deletar publicação', err); },
      });
    }
  }
}