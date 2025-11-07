import { Component, OnInit } from '@angular/core';
import { Prestador, PrestadorService } from '../../services/prestador.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; // 1. Importe o RouterLink aqui

@Component({
  selector: 'app-prestador-list',
  standalone: true,
  imports: [CommonModule, RouterLink], // 2. Adicione o RouterLink aqui
  templateUrl: './prestador-list.html',
  styleUrl: './prestador-list.css'
})
export class PrestadorListComponent implements OnInit {
  // ... (o resto do seu código permanece o mesmo) ...
  prestadores: Prestador[] = [];
  constructor(private prestadorService: PrestadorService) {}
  ngOnInit(): void {
    this.prestadorService.getPrestadores().subscribe(dados => {
      this.prestadores = dados;
    });
  }
}