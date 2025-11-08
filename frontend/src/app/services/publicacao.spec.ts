import { TestBed } from '@angular/core/testing';

import { Publicacao } from './publicacao';

describe('Publicacao', () => {
  let service: Publicacao;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Publicacao);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
