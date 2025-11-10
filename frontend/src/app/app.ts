import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, Router, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth'; // Verifique se o caminho está correto
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators'; // Importação do filter

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class AppComponent {
  title = 'frontend';
  isLoggedIn$: Observable<boolean>;
  currentRoute: string = '';

  constructor(private authService: AuthService, private router: Router) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;

    // CORREÇÃO AQUI:
    // Nós precisamos usar o .pipe() para filtrar os eventos do roteador.
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(event => {
      // Agora o currentRoute será atualizado corretamente.
      this.currentRoute = (event as NavigationEnd).urlAfterRedirects;
    });
  }

  onLogout() {
    this.authService.logout();
  }
}