import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable, switchMap, BehaviorSubject, tap } from 'rxjs'; 
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Prestador, PrestadorService, Publicacao, AvaliacaoResponse } from '../../services/prestador.service';
import { AvaliacaoService } from '../../services/avaliacao';
import { AuthService } from '../../services/auth';
import { PublicacaoService } from '../../services/publicacao';

@Component({
  selector: 'app-prestador-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], 
  templateUrl: './prestador-detail.html',
  styleUrl: './prestador-detail.css'
})
export class PrestadorDetailComponent implements OnInit {

  prestador$!: Observable<Prestador>;
  publicacoes$!: Observable<Publicacao[]>;
  avaliacoes$!: Observable<AvaliacaoResponse[]>;

  private refreshAvaliacoes = new BehaviorSubject<void>(undefined);
  private refreshPublicacoes = new BehaviorSubject<void>(undefined);

  // Status de Login
  isLoggedIn$: Observable<boolean>;
  
  // Formulários
  avaliacaoForm: FormGroup;
  publicacaoForm: FormGroup;
  
  // Variáveis de estado
  prestadorId: number = 0;
  mensagemSucesso: string = '';
  mensagemSucessoPublicacao: string = '';

  // O Construtor é onde inicializamos os formulários
  constructor(
    private route: ActivatedRoute,
    private prestadorService: PrestadorService,
    private avaliacaoService: AvaliacaoService,
    private publicacaoService: PublicacaoService,
    public authService: AuthService,
    private fb: FormBuilder
  ) {
    
    // 1. Inicializa o formulário de avaliação
    this.avaliacaoForm = this.fb.group({
      nota: [5, [Validators.required, Validators.min(1), Validators.max(5)]],
      comentario: ['', Validators.required],
      clienteId: [null, Validators.required] 
    });

    // 2. Inicializa o formulário de publicação (ESTAVA NO LUGAR ERRADO)
    this.publicacaoForm = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      fotoUrl: [''] // Campo opcional
    });
    
    // 3. "Ouve" o status de login (só precisa ser feito uma vez)
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  // ngOnInit é onde buscamos os dados iniciais
  ngOnInit(): void {
    // Pega o ID do prestador da URL uma vez
    const idParam = this.route.snapshot.paramMap.get('id');
    this.prestadorId = Number(idParam);

    if (this.prestadorId) {
      // Busca os dados do prestador
      this.prestador$ = this.prestadorService.getPrestadorById(this.prestadorId);
      
      // Busca as publicações (usando o BehaviorSubject para dar refresh)
      this.publicacoes$ = this.refreshPublicacoes.pipe(
        switchMap(() => this.prestadorService.getPublicacoesPorPrestador(this.prestadorId))
      );
      
      // Busca as avaliações (usando o BehaviorSubject para dar refresh)
      this.avaliacoes$ = this.refreshAvaliacoes.pipe(
        switchMap(() => this.prestadorService.getAvaliacoesPorPrestador(this.prestadorId))
      );
    }
  }

  // Método para enviar a nova publicacao
  onPostarPublicacao() {
    if (this.publicacaoForm.invalid) {
      this.publicacaoForm.markAllAsTouched();
      return;
    }

    this.publicacaoService.salvar(this.prestadorId, this.publicacaoForm.value).subscribe({
      next: () => {
        this.mensagemSucessoPublicacao = 'Publicação criada com sucesso!';
        this.publicacaoForm.reset();
        
        // Força a atualização da lista de publicações
        this.refreshPublicacoes.next(); 
      },
      error: (err) => {
        console.error('Erro ao criar publicação', err);
        this.mensagemSucessoPublicacao = 'Erro ao criar publicação. (Verifique o console)';
      }
    });
  }

  // Método para enviar a nova avaliação
  onPostarAvaliacao() {
    if (this.avaliacaoForm.invalid) {
      this.avaliacaoForm.markAllAsTouched();
      return;
    }

    this.avaliacaoService.salvar(this.prestadorId, this.avaliacaoForm.value).subscribe({
      next: () => {
        this.mensagemSucesso = 'Avaliação postada com sucesso!';
        this.avaliacaoForm.reset({ nota: 5 }); // Reseta o formulário
        
        // Força a atualização da lista de avaliações
        this.refreshAvaliacoes.next(); 
      },
      error: (err) => {
        console.error('Erro ao postar avaliação', err);
        this.mensagemSucesso = 'Erro ao postar avaliação. (Verifique o console)';
      }
    });
  }
}