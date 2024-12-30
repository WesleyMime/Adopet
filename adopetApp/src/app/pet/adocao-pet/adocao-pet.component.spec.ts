import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdocaoPetComponent } from './adocao-pet.component';

describe('AdocaoPetComponent', () => {
  let component: AdocaoPetComponent;
  let fixture: ComponentFixture<AdocaoPetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdocaoPetComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdocaoPetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
