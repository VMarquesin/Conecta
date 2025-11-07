import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestadorDetail } from './prestador-detail';

describe('PrestadorDetail', () => {
  let component: PrestadorDetail;
  let fixture: ComponentFixture<PrestadorDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrestadorDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrestadorDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
