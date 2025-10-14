import { Component, OnInit } from '@angular/core';
import { Prestador, PrestadorService } from '../../services/prestador.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-prestador-list',
  standalone: true, // <-- ADICIONE ESTA LINHA
  imports: [CommonModule],
  templateUrl: './prestador-list.html', // Corrigido na próxima etapa
  styleUrl: './prestador-list.css',
})
export class PrestadorListComponent implements OnInit {
  prestadores: Prestador[] = [];

  constructor(private prestadorService: PrestadorService) {}

  ngOnInit(): void {
    this.prestadorService.getPrestadores().subscribe((dados) => {
      this.prestadores = dados;
    });
  }
}
