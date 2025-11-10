import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

// 1. Importe a classe correta: 'PerfilService'
import { PerfilService } from './perfil'; 

// 2. Corrija o nome no 'describe'
describe('PerfilService', () => {
  // 3. Corrija o tipo da variável
  let service: PerfilService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      // 4. Adicione os 'providers' que o serviço precisa
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    // 5. Injete o serviço correto
    service = TestBed.inject(PerfilService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});