import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestadorList } from './prestador-list';

describe('PrestadorList', () => {
  let component: PrestadorList;
  let fixture: ComponentFixture<PrestadorList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrestadorList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrestadorList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
