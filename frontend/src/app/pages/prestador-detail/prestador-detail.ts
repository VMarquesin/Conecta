import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable, switchMap, BehaviorSubject } from 'rxjs';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

// Nossos Serviços e Interfaces
import {
  Prestador,
  PrestadorService,
  Publicacao,
  AvaliacaoResponse,
} from '../../services/prestador.service';
import { AvaliacaoService } from '../../services/avaliacao';
import { AuthService } from '../../services/auth';
import { PublicacaoService } from '../../services/publicacao';

@Component({
  selector: 'app-prestador-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './prestador-detail.html',
  styleUrl: './prestador-detail.css',
})
export class PrestadorDetailComponent implements OnInit {
  // Observables para os dados da página
  prestador$!: Observable<Prestador>;
  publicacoes$!: Observable<Publicacao[]>;
  avaliacoes$!: Observable<AvaliacaoResponse[]>;

  // BehaviorSubjects para forçar o "refresh"
  private refreshAvaliacoes = new BehaviorSubject<void>(undefined);
  private refreshPublicacoes = new BehaviorSubject<void>(undefined);

  isLoggedIn$: Observable<boolean>;

  // Formulários
  avaliacaoForm: FormGroup; // Para a avaliação do prestador
  publicacaoForm: FormGroup;

  // NOVO FORMULÁRIO (para avaliação da publicação)
  pubAvaliacaoForm: FormGroup;

  // Variáveis de estado
  prestadorId: number = 0;
  mensagemSucesso: string = '';
  mensagemSucessoPublicacao: string = '';

  // Para controlar qual formulário de avaliação de publicação está aberto
  publicacaoAbertaParaAvaliar: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private prestadorService: PrestadorService,
    private avaliacaoService: AvaliacaoService,
    private publicacaoService: PublicacaoService,
    public authService: AuthService,
    private fb: FormBuilder
  ) {
    // Avaliação do PRESTADOR
    this.avaliacaoForm = this.fb.group({
      nota: [5, Validators.required],
      comentario: ['', Validators.required],
    });

    // Nova Publicação
    this.publicacaoForm = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      fotoUrl: [''],
    });

    // NOVO: Avaliação da PUBLICACAO
    this.pubAvaliacaoForm = this.fb.group({
      nota: [5, Validators.required],
      comentario: ['', Validators.required],
    });

    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.prestadorId = Number(idParam);

    if (this.prestadorId > 0) {
      this.prestador$ = this.prestadorService.getPrestadorById(this.prestadorId);
      this.publicacoes$ = this.refreshPublicacoes.pipe(
        switchMap(() => this.prestadorService.getPublicacoesPorPrestador(this.prestadorId))
      );
      this.avaliacoes$ = this.refreshAvaliacoes.pipe(
        switchMap(() => this.prestadorService.getAvaliacoesPorPrestador(this.prestadorId))
      );
    }
  }

  // Método para postar publicação
  onPostarPublicacao() {
    if (this.publicacaoForm.invalid) {
      return;
    }
    this.publicacaoService.salvar(this.prestadorId, this.publicacaoForm.value).subscribe({
      next: () => {
        this.mensagemSucessoPublicacao = 'Publicação criada com sucesso!';
        this.publicacaoForm.reset();
        this.refreshPublicacoes.next();
      },
      error: (err) => {
        /* ... (código de erro) ... */
      },
    });
  }

  // Método para postar avaliação do PRESTADOR
  onPostarAvaliacao() {
    if (this.avaliacaoForm.invalid) {
      return;
    }
    this.avaliacaoService
      .salvarParaPrestador(this.prestadorId, this.avaliacaoForm.value)
      .subscribe({
        next: () => {
          this.mensagemSucesso = 'Avaliação postada com sucesso!';
          this.avaliacaoForm.reset({ nota: 5 });
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          /* ... (código de erro) ... */
        },
      });
  }

  // NOVO MÉTODO: Postar avaliação da PUBLICAÇÃO
  onPostarAvaliacaoPublicacao(publicacaoId: number) {
    if (this.pubAvaliacaoForm.invalid) {
      return;
    }
    this.avaliacaoService
      .salvarParaPublicacao(publicacaoId, this.pubAvaliacaoForm.value)
      .subscribe({
        next: () => {
          alert('Avaliação da publicação enviada com sucesso!');
          this.pubAvaliacaoForm.reset({ nota: 5 });
          this.publicacaoAbertaParaAvaliar = null; // Fecha o formulário
          // Podemos dar refresh em tudo para atualizar contagens, etc.
          this.refreshPublicacoes.next();
        },
        error: (err) => {
          /* ... (código de erro) ... */
        },
      });
  }

  // NOVO MÉTODO: Controla qual formulário de pub. está aberto
  toggleAvaliacaoPublicacao(publicacaoId: number) {
    if (this.publicacaoAbertaParaAvaliar === publicacaoId) {
      this.publicacaoAbertaParaAvaliar = null; // Fecha se já estiver aberto
    } else {
      this.publicacaoAbertaParaAvaliar = publicacaoId; // Abre o novo
      this.pubAvaliacaoForm.reset({ nota: 5 });
    }
  }
  onDeletarPublicacao(publicacaoId: number) {
    if (confirm('Tem certeza que deseja deletar esta publicação?')) {
      this.publicacaoService.deletar(this.prestadorId, publicacaoId).subscribe({
        next: () => {
          alert('Publicação deletada com sucesso!');
          this.refreshPublicacoes.next();
        },
        error: (err) => {
          console.error('Erro ao deletar publicação', err);
          alert('Erro: Você não tem permissão para deletar esta publicação.');
        },
      });
    }
  }

  // NOVO: Método para deletar avaliação
  onDeletarAvaliacao(avaliacaoId: number) {
    if (confirm('Tem certeza que deseja deletar esta avaliação?')) {
      this.avaliacaoService.deletar(avaliacaoId).subscribe({
        next: () => {
          alert('Avaliação deletada com sucesso!');
          // Força o refresh da lista de avaliações
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          console.error('Erro ao deletar avaliação', err);
          alert('Erro: Você não tem permissão para deletar esta avaliação.');
        },
      });
    }
  }
}
