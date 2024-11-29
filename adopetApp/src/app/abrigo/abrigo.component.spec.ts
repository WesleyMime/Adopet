import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbrigoComponent } from './abrigo.component';

describe('AbrigoComponent', () => {
  let component: AbrigoComponent;
  let fixture: ComponentFixture<AbrigoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbrigoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbrigoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
