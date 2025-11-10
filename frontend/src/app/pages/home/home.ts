import { Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent {
  
  // @ViewChild pega o elemento HTML que tem o ID '#faqCarousel'
  // para que possamos manipulá-lo
  @ViewChild('faqCarousel') faqCarousel!: ElementRef;

  /**
   * Rola o carrossel para a esquerda ou direita.
   * @param direction - 'prev' (anterior) ou 'next' (próximo)
   */
  scrollFaq(direction: 'prev' | 'next'): void {
    const carousel = this.faqCarousel.nativeElement as HTMLElement;
    // Pega a largura de um card (assumindo 320px + 1.5rem de gap)
    const scrollAmount = 320 + (16 * 1.5); 

    if (direction === 'prev') {
      carousel.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
    } else {
      carousel.scrollBy({ left: scrollAmount, behavior: 'smooth' });
    }
  }
}