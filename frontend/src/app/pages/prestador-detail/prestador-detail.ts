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
// O PublicacaoService não é mais necessário aqui

@Component({
  selector: 'app-prestador-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './prestador-detail.html',
  styleUrl: './prestador-detail.css',
})
export class PrestadorDetailComponent implements OnInit {
  // Observables para os dados da página (sem publicações, pois está no prestadorService)
  prestador$!: Observable<Prestador>;
  publicacoes$!: Observable<Publicacao[]>;
  avaliacoes$!: Observable<AvaliacaoResponse[]>;

  // BehaviorSubjects para forçar o "refresh"
  private refreshAvaliacoes = new BehaviorSubject<void>(undefined);
  private refreshPublicacoes = new BehaviorSubject<void>(undefined);
  // O refreshPublicacoes não é mais necessário aqui

  isLoggedIn$: Observable<boolean>;

  // Formulários de Avaliação
  avaliacaoForm: FormGroup;
  pubAvaliacaoForm: FormGroup;

  // Variáveis de estado
  prestadorId: number = 0;
  mensagemSucesso: string = '';
  editandoAvaliacaoId: number | null = null;
  publicacaoAbertaParaAvaliar: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private prestadorService: PrestadorService,
    private avaliacaoService: AvaliacaoService,
    // O PublicacaoService foi removido
    public authService: AuthService,
    private fb: FormBuilder
  ) {
    // Avaliação do PRESTADOR
    this.avaliacaoForm = this.fb.group({
      nota: [5, Validators.required],
      comentario: [null],
    });

    // Avaliação da PUBLICACAO
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

      // A busca de publicações (leitura) ainda é necessária
      this.publicacoes$ = this.prestadorService.getPublicacoesPorPrestador(this.prestadorId);

      this.avaliacoes$ = this.refreshAvaliacoes.pipe(
        switchMap(() => this.prestadorService.getAvaliacoesPorPrestador(this.prestadorId))
      );
    }
  }

  // ---------------------------------------------------
  // APENAS MÉTODOS DE AVALIAÇÃO FICAM AQUI
  // ---------------------------------------------------

  // Método para postar avaliação do PRESTADOR

  // Postar avaliação da PUBLICAÇÃO

  onPostarAvaliacao() {
    if (this.avaliacaoForm.invalid) {
      this.avaliacaoForm.markAllAsTouched();
      return;
    }

    // LÓGICA DE EDIÇÃO (UPDATE)
    if (this.editandoAvaliacaoId) {
      const avaliacaoDTO = {
        nota: this.avaliacaoForm.value.nota,
        comentario: this.avaliacaoForm.value.comentario,
      };

      this.avaliacaoService.atualizar(this.editandoAvaliacaoId, avaliacaoDTO).subscribe({
        next: () => {
          alert('Avaliação atualizada com sucesso!');
          this.avaliacaoForm.reset({ nota: 5 });
          this.editandoAvaliacaoId = null; // Sai do modo de edição
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          console.error('Erro ao atualizar:', err);
          alert('Erro ao atualizar. Verifique se você é o dono desta avaliação.');
        },
      });
      return; // Para aqui
    }

    // LÓGICA DE CRIAÇÃO (CREATE) - (Seu código antigo continua aqui)
    const avaliacaoDTO = {
      nota: this.avaliacaoForm.value.nota,
      comentario: this.avaliacaoForm.value.comentario,
    };

    this.avaliacaoService.salvarParaPrestador(this.prestadorId, avaliacaoDTO).subscribe({
      next: () => {
        this.mensagemSucesso = 'Avaliação postada com sucesso!';
        this.avaliacaoForm.reset({ nota: 5 });
        this.refreshAvaliacoes.next();
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  // Controla qual formulário de pub. está aberto
  toggleAvaliacaoPublicacao(publicacaoId: number) {
    if (this.publicacaoAbertaParaAvaliar === publicacaoId) {
      this.publicacaoAbertaParaAvaliar = null;
    } else {
      this.publicacaoAbertaParaAvaliar = publicacaoId;
      this.pubAvaliacaoForm.reset({ nota: 5 });
    }
  }

  // Deletar avaliação
  onDeletarAvaliacao(avaliacaoId: number) {
    if (confirm('Tem certeza que deseja deletar esta avaliação?')) {
      this.avaliacaoService.deletar(avaliacaoId).subscribe({
        next: () => {
          alert('Avaliação deletada com sucesso!');
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          /* ... (código de erro) ... */
        },
      });
    }
  }

  // Carregar avaliação para editar
  onCarregarAvaliacaoParaEditar(avaliacao: AvaliacaoResponse) {
    this.editandoAvaliacaoId = avaliacao.id;
    // Preenche o formulário com os dados existentes
    this.avaliacaoForm.patchValue({
      nota: avaliacao.nota,
      comentario: avaliacao.comentario,
    });
    // Rola a tela até o formulário
    const formElement = document.querySelector('.form-container');
    if (formElement) formElement.scrollIntoView({ behavior: 'smooth' });
  }

  // Cancelar edição da avaliação
  cancelarEdicaoAvaliacao() {
    this.editandoAvaliacaoId = null;
    this.avaliacaoForm.reset({ nota: 5 });
  }
}
