import { TestBed } from '@angular/core/testing';

import { TypesResolver } from './types.resolver';

describe('TypesResolver', () => {
  let resolver: TypesResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(TypesResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
