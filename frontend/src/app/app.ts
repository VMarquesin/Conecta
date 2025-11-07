import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { AuthService } from './services/auth'; // Verifique o caminho
import { Observable } from 'rxjs';


// 1. ADICIONE A IMPORTAÇÃO DE VOLTA
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-root',
  standalone: true,
  
  // 2. ADICIONE O 'CommonModule' DE VOLTA AO ARRAY
  imports: [RouterOutlet, RouterLink, CommonModule], 
  
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  title = 'frontend';
  
  isLoggedIn$: Observable<boolean>;

  constructor(private authService: AuthService) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  onLogout() {
    this.authService.logout();
  }
}