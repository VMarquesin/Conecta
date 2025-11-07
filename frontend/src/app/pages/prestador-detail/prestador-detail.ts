import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable, switchMap, BehaviorSubject } from 'rxjs'; // Importe BehaviorSubject

// Importações de Formulário
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

// Nossos Serviços e Interfaces
import { Prestador, PrestadorService, Publicacao, AvaliacaoResponse } from '../../services/prestador.service';
import { AvaliacaoService } from '../../services/avaliacao';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-prestador-detail',
  standalone: true,
  // Adicione ReactiveFormsModule aos imports
  imports: [CommonModule, ReactiveFormsModule], 
  templateUrl: './prestador-detail.html',
  styleUrl: './prestador-detail.css'
})
export class PrestadorDetailComponent implements OnInit {

  prestador$!: Observable<Prestador>;
  publicacoes$!: Observable<Publicacao[]>;
  avaliacoes$!: Observable<AvaliacaoResponse[]>;

  // Variável para forçar a atualização da lista de avaliações
  private refreshAvaliacoes = new BehaviorSubject<void>(undefined);

  // Status de Login
  isLoggedIn$: Observable<boolean>;
  
  // Formulário de Avaliação
  avaliacaoForm: FormGroup;
  prestadorId: number = 0;
  mensagemSucesso: string = '';

  constructor(
    private route: ActivatedRoute,
    private prestadorService: PrestadorService,
    private avaliacaoService: AvaliacaoService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    // Inicializa o formulário de avaliação
    this.avaliacaoForm = this.fb.group({
      nota: [5, [Validators.required, Validators.min(1), Validators.max(5)]],
      comentario: ['', Validators.required],
      // NOTA: Para um CRUD rápido, estamos pedindo o ID do cliente manualmente.
      // Em uma app real, pegaríamos isso do usuário logado.
      clienteId: [null, Validators.required] 
    });

    // "Ouve" o status de login
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  ngOnInit(): void {
    // Pega o ID do prestador da URL uma vez
    const idParam = this.route.snapshot.paramMap.get('id');
    this.prestadorId = Number(idParam);

    if (this.prestadorId) {
      // Busca os dados do prestador
      this.prestador$ = this.prestadorService.getPrestadorById(this.prestadorId);
      
      // Busca as publicações
      this.publicacoes$ = this.prestadorService.getPublicacoesPorPrestador(this.prestadorId);
      
      // Busca as avaliações e re-busca sempre que 'refreshAvaliacoes' for acionado
      this.avaliacoes$ = this.refreshAvaliacoes.pipe(
        switchMap(() => this.prestadorService.getAvaliacoesPorPrestador(this.prestadorId))
      );
    }
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