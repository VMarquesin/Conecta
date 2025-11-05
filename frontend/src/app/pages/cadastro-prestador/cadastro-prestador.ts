import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormArray,
  FormControl,
} from '@angular/forms';
import { Categoria, CategoriaService } from '../../services/categoria';
import { PrestadorService } from '../../services/prestador.service';

@Component({
  selector: 'app-cadastro-prestador',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro-prestador.html',
  styleUrl: './cadastro-prestador.css',
})
export class CadastroPrestadorComponent implements OnInit {
  cadastroForm: FormGroup;
  categorias: Categoria[] = [];
  mensagemSucesso: string = '';

  constructor(
    private fb: FormBuilder,
    private prestadorService: PrestadorService,
    private categoriaService: CategoriaService
  ) {
    this.cadastroForm = this.fb.group({
      nomeCompleto: ['', Validators.required],
      nomeFantasia: [''],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      cpf: ['', Validators.required],
      bio: [''],
      categoriaIds: this.fb.array([]),
    });
  }

  ngOnInit(): void {
    this.categoriaService.listar().subscribe((categoriasDaApi) => {
      this.categorias = categoriasDaApi; 
      this.atualizarCheckboxesDasCategorias(); 
    });
  }

  private atualizarCheckboxesDasCategorias() {
    const categoriaFormArray = this.cadastroForm.get('categoriaIds') as FormArray;
    this.categorias.forEach(() => {
      categoriaFormArray.push(new FormControl(false));
    });
  }

  // Método envio do formulário
  onSubmit() {
    if (this.cadastroForm.valid) {
      const formValues = this.cadastroForm.value;

      const selectedCategoriaIds = formValues.categoriaIds

        .map((checked: boolean, index: number) => (checked ? this.categorias[index].id : null))
        .filter((id: number | null) => id !== null);

      const prestadorDTO = {
        nomeCompleto: formValues.nomeCompleto,
        nomeFantasia: formValues.nomeFantasia,
        email: formValues.email,
        senha: formValues.senha,
        cpf: formValues.cpf,
        bio: formValues.bio,
        categoriaIds: selectedCategoriaIds, 
      };

      this.prestadorService.salvar(prestadorDTO).subscribe({
        next: (response) => {
          this.mensagemSucesso = 'Prestador cadastrado com sucesso! ID: ' + response.id;
          this.cadastroForm.reset();
        },
        error: (err) => {
          console.error('Erro ao cadastrar prestador', err);
          this.mensagemSucesso = 'Erro ao cadastrar. Verifique o console.';
        },
      });
    } else {
      this.cadastroForm.markAllAsTouched();
    }
  }
}
