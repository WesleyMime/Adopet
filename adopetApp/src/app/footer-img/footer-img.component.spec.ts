import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FooterImgComponent } from './footer-img.component';

describe('FooterImgComponent', () => {
  let component: FooterImgComponent;
  let fixture: ComponentFixture<FooterImgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FooterImgComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FooterImgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
