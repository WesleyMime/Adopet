import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterAbrigoComponent } from './register-abrigo.component';

describe('RegisterAbrigoComponent', () => {
  let component: RegisterAbrigoComponent;
  let fixture: ComponentFixture<RegisterAbrigoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterAbrigoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterAbrigoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
