import { ComponentFixture, TestBed } from '@angular/core/testing';

// 1. Importa o NOME CORRETO
import { MeuPerfilComponent } from './meu-perfil';

// Importa as dependências que o componente precisa
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

// 2. Corrige o NOME CORRETO
describe('MeuPerfilComponent', () => {
  // 3. Corrige o NOME CORRETO
  let component: MeuPerfilComponent;
  let fixture: ComponentFixture<MeuPerfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // 4. Corrige o NOME CORRETO
      imports: [MeuPerfilComponent, ReactiveFormsModule],
      // 5. Adiciona os 'providers' que o componente injeta
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    // 6. Corrige o NOME CORRETO
    fixture = TestBed.createComponent(MeuPerfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});