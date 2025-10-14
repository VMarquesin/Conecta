import { TestBed } from '@angular/core/testing';

import { Prestador } from './prestador';

describe('Prestador', () => {
  let service: Prestador;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Prestador);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
