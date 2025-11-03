import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
// 1. Importe as ferramentas para formulários reativos
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClienteService } from '../../services/cliente';

@Component({
  selector: 'app-cadastro-cliente',
  standalone: true,
  // 2. Adicione ReactiveFormsModule aos imports
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro-cliente.html',
  styleUrl: './cadastro-cliente.css'
})
export class CadastroClienteComponent {
  
  // Declara a variável que vai guardar a estrutura do nosso formulário
  cadastroForm: FormGroup;
  mensagemSucesso: string = '';

  // 3. Injeta o FormBuilder (para criar o formulário) e o ClienteService (para enviar os dados)
  constructor(private fb: FormBuilder, private clienteService: ClienteService) {
    
    // 4. Cria o formulário e define as regras de validação
    this.cadastroForm = this.fb.group({
      nomeCompleto: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      cpf: ['', Validators.required]
    });
  }

  // 5. Método que será chamado quando o formulário for enviado
  onSubmit() {
    if (this.cadastroForm.valid) {
      // Se o formulário for válido, chama o serviço
      this.clienteService.salvar(this.cadastroForm.value).subscribe({
        next: (response) => {
          // Callback de sucesso
          this.mensagemSucesso = 'Cliente cadastrado com sucesso! ID: ' + response.id;
          this.cadastroForm.reset(); // Limpa o formulário
        },
        error: (err) => {
          // Callback de erro
          console.error('Erro ao cadastrar cliente', err);
          this.mensagemSucesso = 'Erro ao cadastrar. Verifique o console.';
        }
      });
    } else {
      // Se o formulário for inválido, marca os campos como "tocados" para exibir os erros
      this.cadastroForm.markAllAsTouched();
    }
  }
}