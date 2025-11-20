import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable, switchMap, BehaviorSubject } from 'rxjs';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Prestador, PrestadorService, Publicacao, AvaliacaoResponse } from '../../services/prestador.service';
import { AvaliacaoService } from '../../services/avaliacao';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-prestador-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './prestador-detail.html',
  styleUrl: './prestador-detail.css',
})
export class PrestadorDetailComponent implements OnInit {
  
  // Dados da Página
  prestador$!: Observable<Prestador>;
  publicacoes$!: Observable<Publicacao[]>;
  avaliacoes$!: Observable<AvaliacaoResponse[]>;

  // Refresh da lista de avaliações
  private refreshAvaliacoes = new BehaviorSubject<void>(undefined);

  isLoggedIn$: Observable<boolean>;
  
  // Formulário de Avaliação (DO PRESTADOR)
  avaliacaoForm: FormGroup;
  
  // Variáveis de estado
  prestadorId: number = 0;
  mensagemSucesso: string = '';
  editandoAvaliacaoId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private prestadorService: PrestadorService,
    private avaliacaoService: AvaliacaoService,
    public authService: AuthService,
    private fb: FormBuilder
  ) {
    // Inicializa APENAS o formulário de avaliação do prestador
    this.avaliacaoForm = this.fb.group({
      nota: [5, Validators.required],
      comentario: ['', Validators.required], // Validador adicionado
    });

    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.prestadorId = Number(idParam);

    if (this.prestadorId > 0) {
      this.prestador$ = this.prestadorService.getPrestadorById(this.prestadorId);
      
      // Apenas busca as publicações (sem refresh, pois é read-only)
      this.publicacoes$ = this.prestadorService.getPublicacoesPorPrestador(this.prestadorId);

      this.avaliacoes$ = this.refreshAvaliacoes.pipe(
        switchMap(() => this.prestadorService.getAvaliacoesPorPrestador(this.prestadorId))
      );
    }
  }

  // --- MÉTODOS DE AVALIAÇÃO ---

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
          this.mensagemSucesso = 'Avaliação atualizada com sucesso!';
          this.cancelarEdicaoAvaliacao(); // Limpa o formulário e o estado
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          console.error('Erro ao atualizar:', err);
          alert('Erro ao atualizar. Verifique se você é o dono desta avaliação.');
        },
      });
      return;
    }

    // LÓGICA DE CRIAÇÃO (CREATE)
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
        alert('Erro ao postar avaliação.');
      },
    });
  }

  onDeletarAvaliacao(avaliacaoId: number) {
    if (confirm('Tem certeza que deseja deletar esta avaliação?')) {
      this.avaliacaoService.deletar(avaliacaoId).subscribe({
        next: () => {
          alert('Avaliação deletada com sucesso!');
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          console.error('Erro ao deletar avaliação', err);
          alert('Erro: Você não tem permissão para deletar esta avaliação.');
        },
      });
    }
  }

  onCarregarAvaliacaoParaEditar(avaliacao: AvaliacaoResponse) {
    this.editandoAvaliacaoId = avaliacao.id;
    this.avaliacaoForm.patchValue({
      nota: avaliacao.nota,
      comentario: avaliacao.comentario,
    });
    const formElement = document.querySelector('.form-container');
    if (formElement) formElement.scrollIntoView({ behavior: 'smooth' });
  }

  cancelarEdicaoAvaliacao() {
    this.editandoAvaliacaoId = null;
    this.avaliacaoForm.reset({ nota: 5 });
    this.mensagemSucesso = '';
  }
}