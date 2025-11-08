import { ComponentFixture, TestBed } from '@angular/core/testing';

// 1. Corrija o nome da classe importada
import { PrestadorDetailComponent } from './prestador-detail';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

// 2. Corrija o nome no describe
describe('PrestadorDetailComponent', () => {
  // 3. Corrija os tipos das variáveis
  let component: PrestadorDetailComponent;
  let fixture: ComponentFixture<PrestadorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // 4. Corrija o nome do componente no imports
      imports: [PrestadorDetailComponent],
      // Adicione os providers que o componente precisa
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]) // Fornece um roteador de teste
      ]
    })
    .compileComponents();
    
    // 5. Corrija o nome do componente aqui
    fixture = TestBed.createComponent(PrestadorDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});