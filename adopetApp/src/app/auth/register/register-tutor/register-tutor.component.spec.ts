import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterTutorComponent } from './register-tutor.component';

describe('RegisterComponent', () => {
  let component: RegisterTutorComponent;
  let fixture: ComponentFixture<RegisterTutorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterTutorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterTutorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
