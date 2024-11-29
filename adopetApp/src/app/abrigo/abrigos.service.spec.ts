import { TestBed } from '@angular/core/testing';

import { AbrigosService } from './abrigos.service';

describe('AbrigosService', () => {
  let service: AbrigosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AbrigosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
