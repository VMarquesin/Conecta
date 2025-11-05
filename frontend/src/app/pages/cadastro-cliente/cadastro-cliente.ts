import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClienteService } from '../../services/cliente';

@Component({
  selector: 'app-cadastro-cliente',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro-cliente.html',
  styleUrl: './cadastro-cliente.css'
})
export class CadastroClienteComponent {
  
  cadastroForm: FormGroup;
  mensagemSucesso: string = '';

  constructor(private fb: FormBuilder, private clienteService: ClienteService) {
    
    this.cadastroForm = this.fb.group({
      nomeCompleto: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      cpf: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.cadastroForm.valid) {
      this.clienteService.salvar(this.cadastroForm.value).subscribe({
        next: (response) => {
          this.mensagemSucesso = 'Cliente cadastrado com sucesso! ID: ' + response.id;
          this.cadastroForm.reset();
        },
        error: (err) => {
          console.error('Erro ao cadastrar cliente', err);
          this.mensagemSucesso = 'Erro ao cadastrar. Verifique o console.';
        }
      });
    } else {
      this.cadastroForm.markAllAsTouched();
    }
  }
}