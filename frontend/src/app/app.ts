import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

// AQUI ESTÁ A ROTA CORRETA:
// Estamos importando a CLASSE 'PrestadorListComponent' do ARQUIVO 'prestador-list.ts'
import { PrestadorListComponent } from './components/prestador-list/prestador-list';

@Component({
  selector: 'app-root',
  standalone: true,
  // E AQUI, adicionamos o componente importado para que possamos usá-lo no template
  imports: [RouterOutlet, PrestadorListComponent, RouterLink],
  templateUrl: './app.html', // Usando o nome do seu arquivo
  styleUrl: './app.css', // Usando o nome do seu arquivo
})
export class AppComponent {
  title = 'frontend';
}
