import { TestBed } from '@angular/core/testing';
// 1. Importe os novos provedores (providers)
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

// Importe o serviço que queremos testar
import { AuthService } from './auth';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      // 2. Substitua o array 'imports' pelo array 'providers'
      providers: [
        provideHttpClient(), // Adiciona o provedor HTTP básico
        provideHttpClientTesting(), // Adiciona o provedor de teste
      ],
    });
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
