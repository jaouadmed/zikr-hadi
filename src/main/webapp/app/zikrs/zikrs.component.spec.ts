import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZikrsComponent } from './zikrs.component';

describe('ZikrsComponent', () => {
  let component: ZikrsComponent;
  let fixture: ComponentFixture<ZikrsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ZikrsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ZikrsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
