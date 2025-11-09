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
  mensagemErro: string | null = null;

  editandoAvaliacaoId: number | null = null;
  // NOVA VARIÁVEL
  editandoPublicacaoId: number | null = null;

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
      comentario: [null]
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

  onCarregarParaEditar(publicacao: Publicacao) {
    console.log('Carregando publicação para edição:', publicacao);

    this.editandoPublicacaoId = publicacao.id;

    this.publicacaoForm.patchValue({
      titulo: publicacao.titulo,
      descricao: publicacao.descricao,
      fotoUrl: publicacao.fotoUrl,
    });

    window.scrollTo({ top: 0, behavior: 'smooth' }); // opcional (smooth)
  }

  // Método para postar publicação
  onPostarPublicacao() {
    if (this.publicacaoForm.invalid) {
      return;
    }

    // 👉 SE houver uma publicação sendo editada, fará UPDATE (PUT)
    if (this.editandoPublicacaoId !== null) {
      this.publicacaoService
        .atualizar(this.prestadorId, this.editandoPublicacaoId, this.publicacaoForm.value)
        .subscribe({
          next: () => {
            this.mensagemSucessoPublicacao = 'Publicação atualizada com sucesso!';
            this.publicacaoForm.reset();
            this.editandoPublicacaoId = null;
            this.refreshPublicacoes.next();
          },
          error: (err) => {
            console.error('Erro ao atualizar publicação:', err);
          },
        });

      return; // impede de cair no POST abaixo
    }

    // 👉 Se NÃO estiver editando, faz o POST (criação)
    this.publicacaoService.salvar(this.prestadorId, this.publicacaoForm.value).subscribe({
      next: () => {
        this.mensagemSucessoPublicacao = 'Publicação criada com sucesso!';
        this.publicacaoForm.reset();
        this.refreshPublicacoes.next();
      },
      error: (err) => {
        console.error('Erro ao criar publicação:', err);
      },
    });
  }

  cancelarEdicao() {
    this.editandoPublicacaoId = null;
    this.publicacaoForm.reset();
  }

  // Método para postar avaliação do PRESTADOR
  // Substitua seu método onPostarAvaliacao por este:
  onPostarAvaliacao() {
    if (this.avaliacaoForm.invalid) {
      this.avaliacaoForm.markAllAsTouched();
      return;
    }

    // Se estamos editando...
    if (this.editandoAvaliacaoId) {
      // ...chame o serviço de ATUALIZAR
      // (Nota: o DTO de atualização não precisa do clienteId)
      const avaliacaoDTO = {
        nota: this.avaliacaoForm.value.nota,
        comentario: this.avaliacaoForm.value.comentario,
      };

      this.avaliacaoService.atualizar(this.editandoAvaliacaoId, avaliacaoDTO).subscribe({
        next: () => {
          this.mensagemSucesso = 'Avaliação ATUALIZADA com sucesso!';
          this.avaliacaoForm.reset({ nota: 5 });
          this.editandoAvaliacaoId = null; // Limpa o modo de edição
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          console.error('Erro ao atualizar avaliação', err);
          this.mensagemSucesso = 'Erro ao atualizar avaliação.';
        },
      });
    } else {
      // ...caso contrário, chame o serviço de SALVAR (Criar)
      // (O DTO de criação precisa do clienteId de teste)
      const avaliacaoDTO = {
        nota: this.avaliacaoForm.value.nota,
        comentario: this.avaliacaoForm.value.comentario,
        clienteId: this.avaliacaoForm.value.clienteId,
      };

      this.avaliacaoService.salvarParaPrestador(this.prestadorId, avaliacaoDTO).subscribe({
        next: () => {
          this.mensagemSucesso = 'Avaliação CRIADA com sucesso!';
          this.avaliacaoForm.reset({ nota: 5 });
          this.refreshAvaliacoes.next();
        },
        error: (err) => {
          console.error('Erro ao atualizar avaliação', err);
          this.mensagemErro = 'Erro ao atualizar avaliação.';
        },
      });
    }
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
  onCarregarAvaliacaoParaEditar(avaliacao: AvaliacaoResponse) {
    this.editandoAvaliacaoId = avaliacao.id;

    this.avaliacaoForm.patchValue({
      nota: avaliacao.nota,
      comentario: avaliacao.comentario,
    });
  }
  // Dentro da classe PrestadorDetailComponent

  cancelarEdicaoAvaliacao() {
    this.editandoAvaliacaoId = null;
    this.avaliacaoForm.reset({ nota: 5 });
  }
}
