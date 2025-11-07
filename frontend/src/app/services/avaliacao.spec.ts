import { TestBed } from '@angular/core/testing';

import { Avaliacao } from './avaliacao';

describe('Avaliacao', () => {
  let service: Avaliacao;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Avaliacao);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
