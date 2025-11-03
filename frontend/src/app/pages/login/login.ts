import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router'; // Importe o Router
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router // Injete o Router para redirecionar o usuário
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          // Login bem-sucedido!
          console.log('Login com sucesso, token:', response.token);
          // Redireciona o usuário para a página inicial
          this.router.navigate(['/']); 
        },
        error: (err) => {
          // Erro de login (ex: senha errada)
          console.error('Erro no login', err);
          this.errorMessage = 'Email ou senha inválidos. Tente novamente.';
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
    }
  }
}