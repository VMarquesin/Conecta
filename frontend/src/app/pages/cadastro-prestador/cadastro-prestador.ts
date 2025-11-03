import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
// 1. Importe tudo o que precisamos para formulários
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormArray,
  FormControl,
} from '@angular/forms';

// 2. Importe os serviços e interfaces que vamos usar
import { Categoria, CategoriaService } from '../../services/categoria';
import { PrestadorService } from '../../services/prestador.service';

@Component({
  selector: 'app-cadastro-prestador',
  standalone: true,
  // 3. Adicione ReactiveFormsModule aos imports
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro-prestador.html',
  styleUrl: './cadastro-prestador.css',
})
export class CadastroPrestadorComponent implements OnInit {
  cadastroForm: FormGroup;
  categorias: Categoria[] = []; // Array para guardar as categorias vindas da API
  mensagemSucesso: string = '';

  constructor(
    private fb: FormBuilder,
    private prestadorService: PrestadorService,
    private categoriaService: CategoriaService
  ) {
    // Inicializa o formulário com campos básicos
    this.cadastroForm = this.fb.group({
      nomeCompleto: ['', Validators.required],
      nomeFantasia: [''],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      cpf: ['', Validators.required],
      bio: [''],
      // 4. Inicializa 'categoriaIds' como um FormArray vazio.
      // Vamos preenchê-lo depois que as categorias chegarem da API.
      categoriaIds: this.fb.array([]),
    });
  }

  // ngOnInit é executado quando o componente carrega
  ngOnInit(): void {
    // 5. Busca a lista de categorias da API
    this.categoriaService.listar().subscribe((categoriasDaApi) => {
      this.categorias = categoriasDaApi; // Salva as categorias
      this.atualizarCheckboxesDasCategorias(); // Atualiza o formulário com elas
    });
  }

  // Cria um FormControl (um checkbox) para cada categoria
  private atualizarCheckboxesDasCategorias() {
    const categoriaFormArray = this.cadastroForm.get('categoriaIds') as FormArray;
    // Para cada categoria, adicionamos um novo FormControl (checkbox) ao FormArray
    this.categorias.forEach(() => {
      categoriaFormArray.push(new FormControl(false)); // Começa desmarcado
    });
  }

  // Método chamado no envio do formulário
  onSubmit() {
    if (this.cadastroForm.valid) {
      // Pega os valores do formulário
      const formValues = this.cadastroForm.value;

      // 6. Mapeia os checkboxes marcados (true) para seus IDs correspondentes
      const selectedCategoriaIds = formValues.categoriaIds
        .map((checked: boolean, index: number) => (checked ? this.categorias[index].id : null))
        .filter((id: number | null) => id !== null);

      // 7. Cria o objeto DTO para enviar à API
      const prestadorDTO = {
        nomeCompleto: formValues.nomeCompleto,
        nomeFantasia: formValues.nomeFantasia,
        email: formValues.email,
        senha: formValues.senha,
        cpf: formValues.cpf,
        bio: formValues.bio,
        categoriaIds: selectedCategoriaIds, // Envia a lista de IDs
      };

      // 8. Envia para o serviço
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
