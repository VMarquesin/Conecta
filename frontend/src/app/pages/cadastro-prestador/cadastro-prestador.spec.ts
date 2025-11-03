import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroPrestador } from './cadastro-prestador';

describe('CadastroPrestador', () => {
  let component: CadastroPrestador;
  let fixture: ComponentFixture<CadastroPrestador>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastroPrestador]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CadastroPrestador);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
