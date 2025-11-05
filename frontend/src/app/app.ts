import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { PrestadorListComponent } from './components/prestador-list/prestador-list';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, PrestadorListComponent, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css', 
})
export class AppComponent {
  title = 'frontend';
}
